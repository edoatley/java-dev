#!/bin/bash

# Keycloak server URL
KEYCLOAK_URL="http://localhost:8080"

# Admin credentials
ADMIN_USERNAME="admin"
ADMIN_PASSWORD="admin"

# Function to obtain admin access token
get_admin_token() {
    curl -s -X POST "$KEYCLOAK_URL/auth/realms/master/protocol/openid-connect/token" \
        -H "Content-Type: application/x-www-form-urlencoded" \
        -d "username=$ADMIN_USERNAME" \
        -d "password=$ADMIN_PASSWORD" \
        -d 'grant_type=password' \
        -d 'client_id=admin-cli' | jq -r '.access_token'
}

# Function to create a new realm
create_realm() {
    local token=$1
    local realm_name=$2

    curl -s -X POST "$KEYCLOAK_URL/auth/admin/realms" \
        -H "Content-Type: application/json" \
        -H "Authorization: Bearer $token" \
        -d "{\"realm\": \"$realm_name\", \"enabled\": true}"
}

# Function to create a new user
create_user() {
    local token=$1
    local realm_name=$2
    local username=$3
    local password=$4

    # Create user
    user_id=$(curl -s -X POST "$KEYCLOAK_URL/auth/admin/realms/$realm_name/users" \
        -H "Content-Type: application/json" \
        -H "Authorization: Bearer $token" \
        -d "{\"username\": \"$username\", \"enabled\": true}" | jq -r '.id')

    # Set user password
    curl -s -X PUT "$KEYCLOAK_URL/auth/admin/realms/$realm_name/users/$user_id/reset-password" \
        -H "Content-Type: application/json" \
        -H "Authorization: Bearer $token" \
        -d "{\"type\": \"password\", \"value\": \"$password\", \"temporary\": false}"
}

# Main script execution
echo "START Realm and test users creation."
admin_token=$(get_admin_token)
realm_name="myrealm"
create_realm "$admin_token" "$realm_name"
echo "Realm $realm_name created."

# Create test users
create_user "$admin_token" "$realm_name" "testuser1" "password1"
echo "User testuser1 created."
create_user "$admin_token" "$realm_name" "testuser2" "password2"
echo "User testuser2 created."

echo "END   Realm and test users created successfully."

# {"error":"Unable to find matching target resource method","error_description":"For more on this error consult the server log at the debug level."}Realm myrealm created.
# {"error":"Unable to find matching target resource method","error_description":"For more on this error consult the server log at the debug level."}User testuser1 created.
# {"error":"Unable to find matching target resource method","error_description":"For more on this error consult the server log at the debug level."}User testuser2 created.