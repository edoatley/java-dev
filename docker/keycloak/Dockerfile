FROM quay.io/keycloak/keycloak:latest

# Copy setup script and make it executable
COPY ./config/setup-keycloak.sh /opt/jboss/
RUN chmod +x /opt/jboss/setup-keycloak.sh

# Set default credentials for keycloak admin user 
ENV KEYCLOAK_ADMIN=admin
ENV KEYCLOAK_ADMIN_PASSWORD=admin

ENTRYPOINT [ "/opt/jboss/setup-keycloak.sh" ]