 #!/bin/bash

IMAGE_NAME="edoatley/java-dev/restapi"
KEYCLOAK_IMAGE_NAME="edoatley/java-dev/keycloak"
IMAGE_VERSION=${1:-v1}
PLATFORM=${2:-linux/amd64}

(cd ../app && ./gradlew build -x integrationTest)

if ! docker buildx build --platform $PLATFORM --tag "$IMAGE_NAME:$IMAGE_VERSION" ../docker; then
    echo "Application build failed"
    exit 1
fi

if ! docker buildx build --platform $PLATFORM --tag "$KEYCLOAK_IMAGE_NAME:$IMAGE_VERSION" ../docker/keycloak; then
    echo "Keycloak build failed"
    exit 1
fi

docker compose up