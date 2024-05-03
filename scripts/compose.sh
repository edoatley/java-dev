 #!/bin/bash

IMAGE_NAME="edoatley/java-dev/restapi"
IMAGE_VERSION=${1:-v1}
PLATFORM=${2:-linux/amd64}

(cd ../app && ./gradlew build -x integrationTest)

if docker buildx build --platform $PLATFORM --tag "$IMAGE_NAME:$IMAGE_VERSION" ../app; then
    docker compose up
else
    echo "Build failed"
    exit 1
fi