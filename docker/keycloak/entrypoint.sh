#!/bin/bash
  
# turn on bash's job control
set -m
  
echo "Starting up keycloak"
echo "Starting up keycloak"
echo "Starting up keycloak"

# Start the KC primary process and put in bg
/opt/keycloak/bin/kc.sh "$@" &
  
echo "Awaiting keycloak"
echo "Awaiting keycloak"
echo "Awaiting keycloak"
# Run the script when the server is up
until curl --output /dev/null --silent --head --fail http://localhost:8080; do
    printf '.'
    sleep 5
done

echo "Setup keycloak"
echo "Setup keycloak"
echo "Setup keycloak"
./opt/jboss/setup-keycloak.sh
  
echo "Keycloak configured"
echo "Keycloak configured"
echo "Keycloak configured"
# now we bring the primary process back into the foreground
# and leave it there
fg %1