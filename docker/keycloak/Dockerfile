# using technique from https://github.com/ASzc/keycloak/blob/f6d532d25c3dc65d6f180382e01bef3b8ec35bfe/docs/guides/src/main/server/containers.adoc#installing-additional-rpm-packages
FROM registry.access.redhat.com/ubi9 AS ubi-micro-build
RUN mkdir -p /mnt/rootfs
RUN dnf install --installroot /mnt/rootfs jq curl --releasever 9 --setopt install_weak_deps=false --nodocs -y && \
    dnf --installroot /mnt/rootfs clean all && \
    rpm --root /mnt/rootfs -e --nodeps setup

FROM quay.io/keycloak/keycloak:latest
COPY --from=ubi-micro-build /mnt/rootfs /

# Set default credentials for keycloak admin user 
ENV KEYCLOAK_ADMIN=admin
ENV KEYCLOAK_ADMIN_PASSWORD=admin

# Copy setup script and make it executable
COPY --chown=keycloak:keycloak ./config/setup-keycloak.sh entrypoint.sh /opt/jboss/
RUN chmod +x /opt/jboss/setup-keycloak.sh && chmod +x /opt/jboss/entrypoint.sh

ENTRYPOINT [ "/opt/jboss/entrypoint.sh" ]