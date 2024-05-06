#!/bin/bash

KEYCLOAK_ADMIN=${1:-"admin"}
KEYCLOAK_ADMIN_PASSWORD=${2:-"admin"}
LOCALPORT=${3:-8080}
KEYCLOAK_LOCAL_IMAGE="local/keycloak:v1"

# Buld keycloak image
docker buildx build --platform linux/amd64 \
  --progress plain \
  --tag $KEYCLOAK_LOCAL_IMAGE \
  .

docker run -p $LOCALPORT:8080 \
  $KEYCLOAK_LOCAL_IMAGE \
  start-dev