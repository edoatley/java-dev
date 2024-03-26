FROM mcr.microsoft.com/devcontainers/java:1-21-bullseye

ARG GRADLE_VERSION=""
RUN su vscode -c "umask 0002 && . /usr/local/sdkman/bin/sdkman-init.sh && sdk install gradle \"${GRADLE_VERSION}\""

# [Optional] Uncomment this section to install additional OS packages.
RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install --no-install-recommends direnv

# Set up Zsh as the default shell
RUN chsh -s $(which zsh)
# Set up direnv hook - Direnv is a tool for managing environment variables, and this configuration ensures that the direnv hook is executed in the shell.
RUN echo 'eval "$(direnv hook zsh)"' >> ~/.zshrc
RUN echo 'eval "$(direnv hook zsh)"' >> ~/.zsh_profile

# [Optional] Uncomment this line to install global node packages.
# RUN su vscode -c "source /usr/local/share/nvm/nvm.sh && npm install -g <your-package-here>" 2>&1
