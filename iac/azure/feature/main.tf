# here we will define a VM on which we will install docker and run the application

# maybe create an RG if you can add a VM to it despite vnet being different RG

module "naming" {
  source  = "Azure/naming/azurerm"
  suffix  = [var.branch_reference]
  version = "0.3.0"
}

resource "azurerm_resource_group" "this" {
  name     = module.naming.resource_group.name
  location = var.location
  tags = {
    "latest-commit" = var.git_short_sha
    "branch"        = var.branch_reference
  }
}

data "azurerm_container_registry" "this" {
  name                = var.acr_name
  resource_group_name = var.static_resource_group_name
}
