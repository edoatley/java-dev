# Authentication for different environments

## Introduction

In a previous article [link]() I looked at how we can use managed identity to authenticate from GitHub actions to Azure. In this follow up
I look to answer the question "How can I authenticate with suitable permissions for the environment and task my GitHub action is performing?"

To achieve this managed identities with appropriate permissions are created and associated with specific GitHub environments. These environments
may be configured with restrictions, such as requiring approval from designated teams or individuals, before they are utilized in a workflow.

## Define the Azure resources