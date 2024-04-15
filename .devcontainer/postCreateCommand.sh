echo 'alias act="act --container-architecture linux/amd64"' >> ~/.zshrc 
echo 'eval "$(direnv hook zsh)"' >> ~/.zshrc
echo "-P ubuntu-latest=catthehacker/ubuntu:act-latest" > ~/.actrc
( cd /workspaces/java-dev && ln -fs ~/gjfpc-hook/pre-commit.sh .git/hooks/pre-commit )
(cd /workspaces/java-dev/app && direnv allow . )
exec zsh