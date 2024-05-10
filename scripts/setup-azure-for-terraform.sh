#!/bin/bash

RESOURCE_GROUP=${1:-"rg-java-dev-tfstate"}
LOCATION=${2:-"uksouth"}
STORAGE_ACCOUNT=${3:-"sajavadevedo6cy6"}
MANAGED_ID_NAME=${4:-"uai-java-dev-terraform"

echo "Checking if resource group ${RESOURCE_GROUP} exists..."
RGEXISTS=$(az group exists --name $RESOURCE_GROUP)
echo "RGEXISTS=${RGEXISTS}"

if [ "$RGEXISTS" == "false" ]
then
    echo "Resource group does not exist. Creating..."
    az group create --name $RESOURCE_GROUP --location $LOCATION
else
    echo "Resource group ${RESOURCE_GROUP} already exists."
fi

echo "Checking if storage account ${STORAGE_ACCOUNT} exists..."
SAEXISTS=$(az storage account show --name $STORAGE_ACCOUNT --resource-group $RESOURCE_GROUP --query "name" --output tsv 2>/dev/null)
echo "STORAGE_ACCOUNT=${STORAGE_ACCOUNT}"

if [ -z "$SAEXISTS" ]
then
    echo "Storage account does not exist. Creating..."
    az storage account create --name $STORAGE_ACCOUNT --resource-group $RESOURCE_GROUP --location $LOCATION --sku Standard_LRS
else
    echo "Storage account already ${STORAGE_ACCOUNT} exists."
fi

az identity create \
  --name "${MANAGED_ID_NAME}" \
  --resource-group "${RESOURCE_GROUP}" \
  --location "${LOCATION}"

az identity federated-credential create \
  --name "${MANAGED_ID_NAME}-fid" \
  --identity-name "${MANAGED_ID_NAME}" \
  --resource-group "${RESOURCE_GROUP}" \
  --issuer 'https://aks.azure.com/issuerGUID' \
  --subject 'repo:edoatley/java-dev:environment:nonprod' \
  --audiences 'api://AzureADTokenExchange'

# grant contributor on the subscription
SUBSCRIPTION_ID=$(az account show --query id -o tsv )
PRINCIPAL_ID=$(az identity show --name $MANAGED_ID_NAME --resource-group $RESOURCE_GROUP --query "principalId" -o tsv)

az role assignment create \
  --role "Contributor" \
  --assignee $PRINCIPAL_ID \
  --scope "/subscriptions/$SUBSCRIPTION_ID"
