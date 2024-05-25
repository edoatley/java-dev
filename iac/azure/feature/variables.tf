variable "branch_reference" {
  type        = string
  description = "The github branch reference"
}

variable "git_short_sha" {
  type        = string
  description = "The github short commit SHA reference"
}

variable "location" {
  type        = string
  description = "The Azure region in which all resources will be created"
  default     = "uksouth"
}

variable "acr_name" {
  type        = string
  description = "The Azure Container Registry name"
}

variable "image_name" {
  type        = string
  description = "The Docker image name"
}

variable "static_resource_group_name" {
  type        = string
  description = "The RG containing static resources"
}
