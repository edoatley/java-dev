echo "Post create command"
echo "Current directory: $(pwd)"
echo "Current user: $(whoami)"
sudo direnv allow /workspaces/java-dev/app
direnv status
cd /workspaces/java-dev/app
direnv status
echo "Post create command completed"