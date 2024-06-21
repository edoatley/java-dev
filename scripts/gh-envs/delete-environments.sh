#!/bin/bash
# FILEPATH: github-action-authn/resources/environments.sh

ORG="edoatley"
REPO="java-dev"

gh api --method DELETE /repos/$ORG/$REPO/environments/dev | jq
gh api --method DELETE /repos/$ORG/$REPO/environments/prod | jq