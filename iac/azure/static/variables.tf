#=========================================================================#
# Basics
#=========================================================================#

variable "resource_group_name" {
  type        = string
  description = "Resource group name"
}

variable "location" {
  type        = string
  description = "Location for all resources"
}

#=========================================================================#
# ACR
#=========================================================================#

variable "acr_name" {
  type        = string
  description = "Azure Container Registry name"
}

variable "acr_sku" {
  type        = string
  description = "Azure Container Registry SKU"
  validation {
    condition     = var.acr_sku == "Basic" || var.acr_sku == "Standard" || var.acr_sku == "Premium"
    error_message = "Invalid SKU. Must be Basic, Standard or Premium"
  }
}

variable "acr_admin_enabled" {
  type        = bool
  description = "Enable admin user for ACR"
}

variable "acr_public_network_access_enabled" {
  type        = bool
  description = "Enable public network access for ACR"
}

#=========================================================================#
# Network
#=========================================================================#

variable "vnet_name" {
  type        = string
  description = "Virtual Network name"
}

variable "vnet_address_space" {
  type        = list(string)
  description = "Address space for the virtual network"
}

variable "nsg_name" {
  type        = string
  description = "Network Security Group name"
}

variable "subnets" {
  type = map(object({
    name             = string
    address_prefixes = list(string)
  }))
  description = "The subnets to create in the virtual network"
}

variable "nsg_rules" {
  type = map(object({
    priority                   = number
    direction                  = string
    access                     = string
    protocol                   = string
    source_port_range          = string
    destination_port_range     = string
    source_address_prefix      = string
    destination_address_prefix = string
  }))
  description = "Network Security Group rules"
}
