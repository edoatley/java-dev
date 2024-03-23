# java-dev

Investigating neat ways to do java cloud native dev

## Plan

- Set up dev container supporting docker, java 21/22, postgresql. See [this article](https://medium.com/@alcbotta/from-an-empty-folder-to-a-complete-application-a-walk-through-using-vscode-remote-container-java-39a6fa6e10e2)
- Build an API (Jetty) that calls multiple backends to get a result and saves it to a database - consider [Structured Concurrency](https://docs.oracle.com/en/java/javase/21/core/structured-concurrency.html#GUID-AA992944-AABA-4CBC-8039-DE5E17DE86DB
- Add basic tests
- Add integration test
- Add CI
- Add basic IaC build (registry)
- Add repository push
- Add proper pipeline for `feature` branches 
- Add full dev IaC

## Dev Container

In the dev contain I wanted the following tooling:

| Tool                 | Version | Reason                           |
| -------------------- | ------- | -------------------------------- |
| Java                 | 21      | Java development and execution   |
| Gradle               | 8.7     | Java build management            |
| Azure CLI            | 2.58.0  | Azure control plane interactions |
| Terraform            | v1.7.5  | Infrastructure as code build     |
| Git                  | 2.44.0  | Source control                   |
| Act                  | 0.2.60  | Run GH workflows locally         |
| Postgresql           |         | Database for App                 |

### Setup

To set up the dev container I utilised the VS Code action `New Dev Container ...`:

![New Dev Container](images/New-Dev-Container.png)

the result is a [.devcontainer folder](.devcontainer) with the following:
- devcontainer.json
- docker-compose.yml
- Dockerfile

### Validation

```bash
# Java Version
vscode ➜ /workspaces/java-dev (setup-devcontainers) $ java -version
openjdk version "21.0.2" 2024-01-16 LTS
OpenJDK Runtime Environment Microsoft-8905927 (build 21.0.2+13-LTS)
OpenJDK 64-Bit Server VM Microsoft-8905927 (build 21.0.2+13-LTS, mixed mode, sharing)

# Gradle Version
vscode ➜ /workspaces/java-dev (setup-devcontainers) $ gradle -version

Welcome to Gradle 8.7!

Here are the highlights of this release:
 - Compiling and testing with Java 22
 - Cacheable Groovy script compilation
 - New methods in lazy collection properties

For more details see https://docs.gradle.org/8.7/release-notes.html


------------------------------------------------------------
Gradle 8.7
------------------------------------------------------------

Build time:   2024-03-22 15:52:46 UTC
Revision:     650af14d7653aa949fce5e886e685efc9cf97c10

Kotlin:       1.9.22
Groovy:       3.0.17
Ant:          Apache Ant(TM) version 1.10.13 compiled on January 4 2023
JVM:          21.0.2 (Microsoft 21.0.2+13-LTS)
OS:           Linux 6.6.16-linuxkit aarch64


# Azure CLI
vscode ➜ /workspaces/java-dev (setup-devcontainers) $ az --version
azure-cli                         2.58.0

core                              2.58.0
telemetry                          1.1.0

Dependencies:
msal                              1.26.0
azure-mgmt-resource             23.1.0b2

Python location '/usr/local/pipx/venvs/azure-cli/bin/python'
Extensions directory '/home/vscode/.azure/cliextensions'

Python (Linux) 3.9.2 (default, Feb 28 2021, 17:03:44) 
[GCC 10.2.1 20210110]

Legal docs and information: aka.ms/AzureCliLegal


Your CLI is up-to-date.

# Terraform Version
vscode ➜ /workspaces/java-dev (setup-devcontainers) $ terraform version
Terraform v1.7.5
on linux_arm64

# Git Version
vscode ➜ /workspaces/java-dev (setup-devcontainers) $ git version
git version 2.44.0

# Act version
vscode ➜ /workspaces/java-dev (setup-devcontainers) $ act --version
act version 0.2.60
```