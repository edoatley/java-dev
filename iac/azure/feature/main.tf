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

data "azurerm_subnet" "this" {
  name                 = "snet-default"
  virtual_network_name = var.static_vnet_name
  resource_group_name  = var.static_resource_group_name
}

resource "azurerm_network_interface" "this" {
  name                = module.naming.network_interface.name
  resource_group_name = azurerm_resource_group.this.name
  location            = azurerm_resource_group.this.location

  ip_configuration {
    name                          = "ipconfig1"
    subnet_id                     = data.azurerm_subnet.this.id
    private_ip_address_allocation = "Dynamic"
  }
}

resource "azurerm_linux_virtual_machine" "this" {
  name                = module.naming.virtual_machine.name
  resource_group_name = azurerm_resource_group.this.name
  location            = azurerm_resource_group.this.location
  size                = var.vm_size
  admin_username      = var.vm_admin_username

  admin_ssh_key {
    username   = var.vm_admin_username
    public_key = var.vm_ssh_public_key
  }

  os_disk {
    caching              = "ReadWrite"
    storage_account_type = "Standard_LRS"
  }

  source_image_reference {
    publisher = "Canonical"
    offer     = "UbuntuServer"
    sku       = "18.04-LTS"
    version   = "latest"
  }

  network_interface_ids = [azurerm_network_interface.this.id]

  tags = {
    "latest-commit" = var.git_short_sha
    "branch"        = var.branch_reference
  }

  user_data = file("${path.module}./scripts/user-data.sh")

}
