ARG IMAGE_TAG=1-21-bullseye
FROM mcr.microsoft.com/devcontainers/java:$IMAGE_TAG

ARG GRADLE_VERSION=""
RUN su vscode -c "umask 0002 && . /usr/local/sdkman/bin/sdkman-init.sh && sdk install gradle \"${GRADLE_VERSION}\""

# [Optional] Uncomment this section to install additional OS packages.
RUN apt-get update \
    && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install --no-install-recommends \
    direnv

# Set up Zsh as the default shell
RUN chsh -s $(which zsh)
# Set up direnv hook - Direnv is a tool for managing environment variables, and this configuration ensures that the direnv hook is executed in the shell.
RUN echo 'eval "$(direnv hook zsh)"' >> ~/.zshrc

RUN git clone https://github.com/justinludwig/gjfpc-hook.git ~/gjfpc-hook
