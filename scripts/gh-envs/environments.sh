#!/bin/bash
# FILEPATH: github-action-authn/resources/environments.sh

#########################################################################################################
# Variables
#########################################################################################################

# Azure details
RESOURCE_GROUP="rg-edo-idtest-control-plane"
PROD_MANAGED_ID_NAME="uai-idtest-prod"
DEV_MANAGED_ID_NAME="uai-idtest-dev"

# GitHub details
PROD_REVIEW_TEAM="sre-team"
REVIEW_WAIT_TIMER=30
ORG="edoatley"
REPO="java-dev"

# Directory of this script
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
echo "Running script in: $DIR"

#########################################################################################################
# Functions
#########################################################################################################

add_client_id_secret_to_gh_env() {
  local gh_env=$1
  local client_id=$2
  local ORG=$3
  local REPO=$4

  echo "Fetching ${gh_env} environment public key..."
  local env_public_key_full=$(gh api /repos/$ORG/$REPO/environments/${gh_env}/secrets/public-key | jq)
  local env_public_key=$(jq -r ".key" <<< "$env_public_key_full")
  local env_public_key_id=$(jq -r ".key_id" <<< "$env_public_key_full")

  echo "Encrypting ${gh_env} client ID..."
  local client_id_enc=$(python3 "${DIR}/encrypt.py" "$env_public_key" "$client_id")

  echo "Saving ${gh_env} client ID as environment secret..."
  gh api --method PUT /repos/$ORG/$REPO/environments/${gh_env}/secrets/AZURE_TERRAFORM_CLIENT_ID \
     -f "encrypted_value=$client_id_enc" \
     -f "key_id=$env_public_key_id" | jq
}

add_repository_secret() {
  local secret_name=$1
  local secret_value=$2
  local ORG=$3
  local REPO=$4

  echo "Fetching repository public key..."
  local repo_public_key_full=$(gh api /repos/$ORG/$REPO/actions/secrets/public-key | jq)
  local repo_public_key=$(jq -r ".key" <<< "$repo_public_key_full")
  local repo_public_key_id=$(jq -r ".key_id" <<< "$repo_public_key_full")

  echo "Encrypting $secret_name..."
  local secret_value_enc=$(python3 "${DIR}/encrypt.py" "$repo_public_key" "$secret_value")

  echo "Saving $secret_name as repository secret..."
  gh api --method PUT /repos/$ORG/$REPO/actions/secrets/$secret_name \
     -f "encrypted_value=$secret_value_enc" \
     -f "key_id=$repo_public_key_id" | jq
}

#########################################################################################################
# Environments
#########################################################################################################

# Create the DEV environment
echo "Creating DEV environment..."
gh api --method PUT /repos/$ORG/$REPO/environments/dev | jq

# Create the PROD environment
echo "Creating PROD environment..."
team_id=$(gh api orgs/$ORG/teams --paginate | jq ".[] | select(.name==\"$PROD_REVIEW_TEAM\").id")
gh api --method PUT /repos/$ORG/$REPO/environments/prod \
  -F "wait_timer=$REVIEW_WAIT_TIMER" \
  -F "prevent_self_review=false" \
  -F "reviewers[][type]=Team" -F "reviewers[][id]=$team_id" | jq

#########################################################################################################
# Set Repository Secrets
#########################################################################################################
# Here we set the tenant and subscription as repository secrets for our purposes but if they need to vary
# by environment we could set in the GH environment

echo "Fetching Azure subscription and tenant IDs..."
subscription_id=$(az account show --query id -o tsv)
tenant_id=$(az account show --query tenantId -o tsv)

echo "Saving Azure Tenant ID as repository secret..."
add_repository_secret "AZURE_TENANT_ID" "$tenant_id" "$ORG" "$REPO"

echo "Saving Azure Subscription ID as repository secret..."
add_repository_secret "AZURE_SUBSCRIPTION_ID" "$subscription_id" "$ORG" "$REPO"

#########################################################################################################
# Set Environment Secrets
#########################################################################################################

echo "Fetching dev environment managed id client ID ..."
dev_client_id=$(az identity show --name "${PROD_MANAGED_ID_NAME}" --resource-group "${RESOURCE_GROUP}" --query "clientId" -o tsv)

echo "Saving dev environment managed id client ID as environment secret..."
add_client_id_secret_to_gh_env "dev" "${dev_client_id}" "$ORG" "$REPO"

echo "Fetching prod managed identity client ID..."
prod_client_id=$(az identity show --name "${DEV_MANAGED_ID_NAME}" --resource-group "${RESOURCE_GROUP}" --query "clientId" -o tsv)

echo "Saving prod environment managed id client ID as environment secret..."
add_client_id_secret_to_gh_env "prod" "${prod_client_id}" "$ORG" "$REPO"
