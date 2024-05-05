#!/bin/bash

KEYCLOAK_ADMIN=${1:-"admin"}
KEYCLOAK_ADMIN_PASSWORD=${2:-"admin"}
LOCALPORT=${3:-8080}
KEYCLOAK_LOCAL_IMAGE="local/keycloak:v1"
KEYCLOAK_REALM_FILE='$(pwd)/config/dev-realm-export.json'

# Buld keycloak image
docker buildx build --platform linux/amd64 \
  --build-arg "REALM_FILE=$KEYCLOAK_REALM_FILE" \
  --tag $KEYCLOAK_LOCAL_IMAGE \
  .

docker run -p $LOCALPORT:8080 \
  -e KEYCLOAK_ADMIN=$KEYCLOAK_ADMIN \
  -e KEYCLOAK_ADMIN_PASSWORD=$KEYCLOAK_ADMIN_PASSWORD \
  $KEYCLOAK_LOCAL_IMAGE \
  start-dev