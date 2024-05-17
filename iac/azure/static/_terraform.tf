terraform {

  backend "azurerm" {}
  required_version = ">= 1.5.7"

  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = ">=3.102.0"
    }
    local = {
      source  = "hashicorp/local"
      version = ">=2.3.0"
    }
  }
}
