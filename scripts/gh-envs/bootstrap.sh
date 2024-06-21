#!/bin/bash
# FILEPATH: resources/gh-envs/bootstrap.sh

RESOURCE_GROUP="rg-edo-idtest-control-plane"
LOCATION="northeurope"

STORAGE_ACCOUNT="saedotfstatece45"
PROD_SA_CONTAINER="prod"
DEV_SA_CONTAINER="dev"

PROD_MANAGED_ID_NAME="uai-idtest-prod"
DEV_MANAGED_ID_NAME="uai-idtest-dev"

GH_REPO="edoatley/java-dev"

# Define the resource group 
echo "Create the resource group..."
az group create --name "$RESOURCE_GROUP" --location "$LOCATION"

# Define the storage account 
echo "Creating the storage account..."
az storage account create --name "${STORAGE_ACCOUNT}" --resource-group "$RESOURCE_GROUP" --location "$LOCATION" --sku "Standard_LRS"
account_key=$(az storage account keys list --resource-group "$RESOURCE_GROUP" --account-name "$STORAGE_ACCOUNT" --query "[0].value" -o tsv)
az storage container create --name "${PROD_SA_CONTAINER}" --account-name "${STORAGE_ACCOUNT}" --account-key "${account_key}"
az storage container create --name "${DEV_SA_CONTAINER}" --account-name "${STORAGE_ACCOUNT}" --account-key "${account_key}"

# Define the managed identities and federate the identities
az identity create --name "${PROD_MANAGED_ID_NAME}" --resource-group "${RESOURCE_GROUP}" --location "${LOCATION}" 
az identity federated-credential create \
    --name "${PROD_MANAGED_ID_NAME}-fid" \
    --identity-name "${PROD_MANAGED_ID_NAME}" \
    --resource-group "${RESOURCE_GROUP}" \
    --issuer 'https://token.actions.githubusercontent.com' \
    --subject "repo:$GH_REPO:environment:prod" \
    --audiences 'api://AzureADTokenExchange'

az identity create --name "${DEV_MANAGED_ID_NAME}" --resource-group "${RESOURCE_GROUP}" --location "${LOCATION}"
az identity federated-credential create \
    --name "${DEV_MANAGED_ID_NAME}-fid" \
    --identity-name "${DEV_MANAGED_ID_NAME}" \
    --resource-group "${RESOURCE_GROUP}" \
    --issuer 'https://token.actions.githubusercontent.com' \
    --subject "repo:$GH_REPO:environment:dev" \
    --audiences 'api://AzureADTokenExchange'

SUBSCRIPTION_ID=$(az account show --query id -o tsv )

# Create the PROD RG and assign the managed identity Contributor
PROD_RESOURCE_GROUP="rg-edo-idtest-prod"
az group create --name "$PROD_RESOURCE_GROUP" --location "$LOCATION"

PROD_PRINCIPAL_ID=$(az identity show --name "${PROD_MANAGED_ID_NAME}" --resource-group "${RESOURCE_GROUP}" --query "principalId" -o tsv)
az role assignment create \
    --role "Contributor" \
    --assignee "$PROD_PRINCIPAL_ID" \
    --scope "/subscriptions/${SUBSCRIPTION_ID}/resourceGroups/${PROD_RESOURCE_GROUP}"

# Create the DEV RG and assign the managed identity Contributor
DEV_RESOURCE_GROUP="rg-edo-idtest-dev"
az group create --name "$DEV_RESOURCE_GROUP" --location "$LOCATION"

DEV_PRINCIPAL_ID=$(az identity show --name "${DEV_MANAGED_ID_NAME}" --resource-group "${RESOURCE_GROUP}" --query "principalId" -o tsv)
az role assignment create \
    --role "Contributor" \
    --assignee "$DEV_PRINCIPAL_ID" \
    --scope "/subscriptions/${SUBSCRIPTION_ID}/resourceGroups/${DEV_RESOURCE_GROUP}"