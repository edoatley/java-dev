resource_group_name               = "rg-static-java-dev-azure-iac"
location                          = "uksouth"
acr_name                          = "statjavadev9ie9"
acr_sku                           = "Basic"
acr_admin_enabled                 = false
acr_public_network_access_enabled = true
vnet_name                         = "vnet-java-dev-azure-iac"
vnet_address_space                = ["10.10.0.0/16"]
nsg_name                          = "nsg-java-dev-azure-iac"
subnets = {
  default = {
    name             = "snet-default"
    address_prefixes = ["10.10.0.0/24"]
  }
}
nsg_rules = {
  basic-rule = {
    priority                   = 100
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_address_prefix      = "*"
    source_port_range          = "*"
    destination_address_prefix = "10.10.0.0/24"
    destination_port_range     = "80"
  }
}
