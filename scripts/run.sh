#!/bin/bash

####################################################################################################
# Variables
####################################################################################################
BASE_IMAGE=${1:-"azul/zulu-openjdk-alpine:21"}
DOCKER_FILE=${2:-"Dockerfile"}
JKS_LOCATION=${3:-"src/itest/resources/itest-keystore.jks"}
JKS_PASSWORD=${4:-"itestjks"}
JAR_LOCATION="build/libs/restapi-all.jar"
HOST_NAME="integration.restapi.edoatley.com"

####################################################################################################
# Functions
####################################################################################################
findFreePort() {
  local port
  port=$(shuf -i 1024-49151 -n 1)
  while [[ $(lsof -i:$port) ]]; do
    port=$(shuf -i 1024-49151 -n 1)
  done
  echo $port
}

function strikingPrint() {
    msg=$1
    msg_length=${#msg}
    echo ""
    printf "%s\n" "$(printf "=%.0s" $(seq 1 "${msg_length}"))"
    echo "$msg"
    printf "%s\n" "$(printf "=%.0s" $(seq 1 "${msg_length}"))"
    echo ""
}

####################################################################################################
# Useful calculated variables
####################################################################################################
short_git_sha=$(git rev-parse --short HEAD)
free_port=$(findFreePort)
short_datetime=$(date '+%Y%m%d-%H%M%S')
logfile="${short_datetime}-${short_git_sha}.log"
image_name="restapi:${short_git_sha}"

####################################################################################################
# Redirect all output to a log file
####################################################################################################
exec >> ../logs/$logfile 2>&1

####################################################################################################
# Step 1 Build the application
####################################################################################################
strikingPrint "Step 1: Building the application" $blue
cd ../app
./gradlew clean build -x integrationTest

####################################################################################################
# Step 2 Dockerise the application
####################################################################################################
strikingPrint "Step 2: Dockerising the application" $blue

docker buildx build \
  --no-cache \
  --platform linux/arm64 \
  --progress plain \
  --build-arg JAR_FILE_LOCN=$JAR_LOCATION \
  --build-arg BASE_IMAGE=$BASE_IMAGE \
  --file $DOCKER_FILE \
  --tag ${image_name} \
  .

if [ $? -ne 0 ]; then
  echo "Docker build failed"
  exit 1
fi

sleep 5

####################################################################################################
# Step 3 Run the application
####################################################################################################
strikingPrint "Step 3: Running the application" $blue
# if the restapi container is there (from a previous run) stop and remove it
if [ $(docker ps -a -q -f name=restapi) ]; then
  docker stop restapi
  docker rm restapi
fi

docker run -d \
  -p ${free_port}:8443 \
  --mount type=bind,source=$(pwd)/${JKS_LOCATION},target=/home/app/tls/keystore.jks,readonly \
  --env SERVER_KEYSTORE_PASSWORD=${JKS_PASSWORD} \
  --name restapi ${image_name}

if [ $? -ne 0 ]; then
  echo "Docker run failed"
  exit 1
fi

until [ "$(docker inspect -f {{.State.Running}} restapi)"=="true" ]; do
    sleep 1;
done;

####################################################################################################
# Step 4 call the application using curl
####################################################################################################
strikingPrint "Step 4: Testing the application" $blue

# Try 5 times 5 seconds apart and fail if none succeed
for i in {1..5}; do
  curl -vvv -k \
      -H "Host: ${HOST_NAME}" \
      -H "Content-Type: application/json" \
      -d '{"username":"user","password":"password"}' \
      "https://localhost:$free_port/api/authentication" && break || sleep 5
done

if [ $? -ne 0 ]; then
  echo "CURL test failed after 5 attempts"
  exit 1
fi

####################################################################################################
# Step 5 run gradle integration tests
####################################################################################################
strikingPrint "Step 5: Running the integration tests" $blue
SERVER_PORT=${free_port} \
SERVER_KEYSTORE=${JKS_LOCATION} \
SERVER_KEYSTORE_PASSWORD=${JKS_PASSWORD} \
SERVER_HOSTNAME=${HOST_NAME} \
  ./gradlew integrationTest