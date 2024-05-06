# using technique from https://github.com/ASzc/keycloak/blob/f6d532d25c3dc65d6f180382e01bef3b8ec35bfe/docs/guides/src/main/server/containers.adoc#installing-additional-rpm-packages
FROM registry.access.redhat.com/ubi9 AS ubi-micro-build

# Script uses curl and jq so install them
RUN mkdir -p /mnt/rootfs \
&& dnf install --installroot /mnt/rootfs jq curl libcurl3 \
--releasever 9 --setopt install_weak_deps=false --nodocs -y; \
dnf --installroot /mnt/rootfs clean all

FROM quay.io/keycloak/keycloak:latest
COPY --from=ubi-micro-build --chown=keycloak:keycloak /usr/bin/curl /usr/bin/curl
# COPY --from=ubi-micro-build /usr/bin/jq /usr/bin/jq
RUN chmod +x /usr/bin/curl

# Set default credentials for keycloak admin user 
ENV KEYCLOAK_ADMIN=admin
ENV KEYCLOAK_ADMIN_PASSWORD=admin

# Copy setup script and make it executable
COPY --chown=keycloak:keycloak ./config/setup-keycloak.sh /opt/jboss/
RUN chmod +x /opt/jboss/setup-keycloak.sh

RUN curl

ENTRYPOINT [ "/opt/jboss/setup-keycloak.sh" ]