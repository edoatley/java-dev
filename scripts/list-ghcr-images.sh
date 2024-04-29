#!/bin/bash

# variable
user=edoatley

gh auth login
containers=$(gh api /users/$user/packages?package_type=container | jq -r .[].name)
for container in $containers; do
    gh api /users/$user/packages/container/$container/versions | less
done
