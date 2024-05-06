FROM quay.io/keycloak/keycloak:latest

COPY --chown=keycloak:keycloak ./config/test-realm.json /opt/keycloak/data/import/

# Set default credentials for keycloak admin user 
ENV KEYCLOAK_ADMIN=admin
ENV KEYCLOAK_ADMIN_PASSWORD=admin