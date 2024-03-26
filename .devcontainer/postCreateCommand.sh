echo 'alias act="act --container-architecture linux/amd64"' >> ~/.zshrc 
echo 'eval "$(direnv hook zsh)"' >> ~/.zshrc
echo 'eval "$(direnv hook zsh)"' >> ~/.zsh_profile
direnv allow /workspaces/java-dev
echo "-P ubuntu-latest=catthehacker/ubuntu:act-latest" > ~/.actrc 
exec zsh