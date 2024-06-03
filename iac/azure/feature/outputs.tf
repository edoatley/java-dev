output "vm_name" {
  description = "The name of the virtual machine"
  value       = azurerm_linux_virtual_machine.this.name
}

output "private_ip" {
  description = "The private IP address of the virtual machine"
  value       = azurerm_network_interface.this.private_ip_address
}
