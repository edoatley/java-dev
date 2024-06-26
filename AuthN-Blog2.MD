# Authentication for different environments

- [Authentication for different environments](#authentication-for-different-environments)
  - [Introduction](#introduction)
  - [Define the Azure resources](#define-the-azure-resources)
  - [Configure GitHub Environments](#configure-github-environments)
  - [Set up secrets for Azure CLI Login](#set-up-secrets-for-azure-cli-login)
  - [Validation](#validation)
  - [Conclusion](#conclusion)


## Introduction

In a previous article [link]() I looked at how we can use managed identity to authenticate from GitHub actions to Azure. In this follow up
I look to answer the question "How can I authenticate with suitable permissions for the environment and task my GitHub action is performing?"

To achieve this managed identities with appropriate permissions are created and associated with specific GitHub environments. These environments
may be configured with restrictions, such as requiring approval from designated teams or individuals, before they are utilized in a workflow.

## Define the Azure resources

Before creating the Azure resources, we define the different permission sets that we want to utilise. In this case I am going to define a very simple
scenario where we have a development and production environment. To manage the resources in these environments we create these identities: 

| Resource Group                | Resource          | Resource Type          | Notes                               |
| --- | --- | --- | --- | 
| rg-edo-idtest-control-plane   | uai-idtest-prod   | User-assigned identity | Identity with access to production  |  
| rg-edo-idtest-control-plane   | uai-idtest-dev    | User-assigned identity | Identity with access to development |  

To mimic a `dev` and a `prod` environment we will create these resource groups:

- `rg-edo-idtest-dev` - to hold the development resources
- `rg-edo-idtest-prod` - to hold the production resources

and then create the **Contributor** role assignments for each identity so it can manage the resources it should:

| Identity          | Access                         | Scope         |
| --- | --- | --- |
| uai-idtest-dev    | Contributor                    | `/subscriptions/${SUBSCRIPTION_ID}/resourceGroups/rg-edo-idtest-dev`  |
| uai-idtest-prod   | Contributor                    | `/subscriptions/${SUBSCRIPTION_ID}/resourceGroups/rg-edo-idtest-prod` |

> Note: In a more realistic solution identities could be defined for different subscriptions or for different privileged tasks. 

When the workflow runs, GitHub generates an OIDC token for the job. This token is signed by GitHub and contains claims that identify 
the GitHub repository and environment details. To allow Azure (Entra ID) to trust the token we need to configure the managed identities
to GitHub by configuring federated identity details:

| Identity          | GH Environment | GH Repository     | Token Audience             |
| --- | --- | --- | --- |
| uai-idtest-prod   | prod           | edoatley/java-dev | api://AzureADTokenExchange |
| uai-idtest-dev    | dev            | edoatley/java-dev | api://AzureADTokenExchange |

With this configured the claims in the token will match those Entra ID is expecting and allow the workflow to be authenticated
and the associated permissions of the managed identity.

## Configure GitHub Environments

With the Azure side configured we now need to set up GitHub to match. For this we will make use of the [GitHub CLI](https://cli.github.com/manual/).
For ease and generalisation we will set the GITHUB_TOKEN, ORG and REPO as environment variables. The `GITHUB_TOKEN` should be a PAT token with `repo` 
and `workflow` permissions:

```bash
GITHUB_TOKEN="token_value"
ORG="edoatley"
REPO="java-dev"
```

With `GITHUB_TOKEN` set `gh` commands will use this to authenticate (or you can use `gh auth login`) so we can use it to set up the `dev` and `prod` GitHub environments:

```bash
# Create Development GH environment
gh api --method PUT /repos/$ORG/$REPO/environments/dev 

# fetch the identifier of the **prod-approvers** team

team_id=$(gh api orgs/$ORG/teams --paginate | jq '.[] | select(.name=="prod-approvers").id')

# Create Production GH environment
gh api --method PUT /repos/$ORG/$REPO/environments/prod \
  -F "prevent_self_review=false" \
  -F "reviewers[][type]=Team" -F "reviewers[][id]=$team_id"
```

> Note: for production we want more control and this configuration will require a review before allowing a deployment to use the
> `prod` GitHub environment

## Set up secrets for Azure CLI Login

To login to Azure using the GitHub generated OIDC token we can use the following code:

```yaml
- name: Login to Azure  
  uses: azure/login@v2
  with:
    client-id: ${{ secrets.AZURE_CLIENT_ID }}          
    tenant-id: ${{ secrets.AZURE_TENANT_ID }}
    subscription-id: ${{ secrets.AZURE_SUBSCRIPTION_ID }}
```

So the next step is to set these secrets up in GitHub so they can be used in the workflow that will call this action. In this 
case the Azure tenant and subscription are the same for this set up and can be created as repository level secrets. The value of 
`AZURE_CLIENT_ID` needs to match that of the managed identity and so will be set up as GitHub environment secrets.

To store secrets programmatically by calling the GitHub REST API, first encrypt the secrets with LibSodium as described [here](https://docs.github.com/en/rest/guides/encrypting-secrets-for-the-rest-api?apiVersion=2022-11-28). In essence it is a 3 part process:

1. Get the public key which can be for the organization, repository or environment depending where you are storing the secret.
2. Use LibSodium to encrypt the secret with the key
3. Save the encrypted secret to the organization, repository or environment depending on the desired scope

To obtain the key we can use this script:

```bash
repo_public_key_full=$(gh api /repos/$ORG/$REPO/actions/secrets/public-key | jq)
repo_public_key=$(jq -r ".key" <<< "$repo_public_key_full")
repo_public_key_id=$(jq -r ".key_id" <<< "$repo_public_key_full")
```

> Note: For the environment level key we can use `gh api /repos/$ORG/$REPO/environments/${GH_ENV}/secrets/public-key`

Next to encrypt the secret we install `pynacl`:

```bash
pip3 install pynacl
```

save this script to a file `encrypt.py`:

```python
import sys
from base64 import b64encode
from nacl import encoding, public

def encrypt(public_key: str, secret_value: str) -> str:
  """Encrypt a Unicode string using the public key."""
  public_key = public.PublicKey(public_key.encode("utf-8"), encoding.Base64Encoder())
  sealed_box = public.SealedBox(public_key)
  encrypted = sealed_box.encrypt(secret_value.encode("utf-8"))
  return b64encode(encrypted).decode("utf-8")

if __name__ == "__main__":
  if len(sys.argv) != 3:
    sys.stderr.write("Usage: %s PUBLIC_KEY SECRET_VALUE\n" % sys.argv[0])
    sys.exit(1)
  print(encrypt(sys.argv[1], sys.argv[2]))
```

and then generate our encrypted tenant and subscription IDs with the repository key and save to GitHub:

```bash
tenant_id_enc=$(python3 encrypt.py "$public_key" "$tenant_id")
gh api --method PUT /repos/$ORG/$REPO/actions/secrets/AZURE_TENANT_ID \
     -f "encrypted_value=$tenant_id_enc" -f "key_id=$public_key_id"

subscription_id_enc=$(python3 encrypt.py "$public_key" "$subscription_id")
gh api --method PUT /repos/$ORG/$REPO/actions/secrets/AZURE_SUBSCRIPTION_ID \
     -f "encrypted_value=$subscription_id_enc" -f "key_id=$public_key_id"
```

We can add the client IDs to the environment in a very similar way. Below is the code to do so for `dev`:

```bash
# Retrieve client id
dev_client_id=$(az identity show --name uai-idtest-dev  --resource-group rg-edo-idtest-control-plane --query "clientId" -o tsv)

dev_env_public_key_full=$(gh api /repos/$ORG/$REPO/environments/dev/secrets/public-key | jq)
dev_env_public_key=$(jq -r ".key" <<< "$dev_env_public_key_full")
dev_env_public_key_id=$(jq -r ".key_id" <<< "$dev_env_public_key_full")

# Encrypting dev client ID...
dev_client_id_enc=$(python3 encrypt.py "$env_public_key" "$dev_client_id")

# Saving dev client ID as environment secret...
gh api --method PUT /repos/$ORG/$REPO/environments/dev/secrets/AZURE_CLIENT_ID \
    -f "encrypted_value=$dev_client_id_enc" -f "key_id=$env_public_key_id"
```

repeating for the `prod` client id we have all our secrets defined.

## Validation

To validate this all works I created the following workflow:

```yaml
name: Azure CLI test
on:
  workflow_dispatch: 
    inputs:
      environment:
        description: 'Github Environment to use'
        required: true
        type: choice
        options:
          - 'dev'
          - 'prod'    

          permissions:
  id-token: write
  contents: read

jobs:
  azure-cli-test:
    runs-on: ubuntu-latest
    environment: ${{ inputs.environment }}
    defaults:
      run:
        shell: bash
    steps:
      - name: Checkout repo code
        uses: actions/checkout@v4
      - name: Login to Azure
        uses: azure/login@v2
        with:
          client-id: ${{ secrets.AZURE_CLIENT_ID }}          
          tenant-id: ${{ secrets.AZURE_TENANT_ID }}
          subscription-id: ${{ secrets.AZURE_SUBSCRIPTION_ID }}
      - name: Create storage account
        run: az storage account create --name edoidtest${{ inputs.environment }}zz11 --resource-group rg-edo-idtest-${{ inputs.environment }} --location uksouth --sku Standard_LRS 
      - name: Create storage account in other environment
        run: |
          if [ "${{ inputs.environment }}" == "dev" ]; then
            az storage account create --name edoidtestprodzz22 --resource-group rg-edo-idtest-prod --location uksouth --sku Standard_LRS || true
          else
            az storage account create --name edoidtestdevzz22 --resource-group rg-edo-idtest-dev --location uksouth --sku Standard_LRS || true
          fi       
```

We should now be able to run with the environment set to `dev` or `prod` and see the storage account be created successfully for each in the step
**Create storage account** and fail to create a resource in step **Create storage account in other environment**




## Conclusion