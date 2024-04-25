# Use a minimal base image
ARG JAR_LOCATION=build/libs/restapi-all.jar
FROM azul/zulu-openjdk-alpine:21

# Non-root user for running the application
RUN addgroup -S app && adduser -S app -G app
USER app:app

# No need to run software as privileged
RUN chmod 755 /home/app

# Copy the jar file
COPY --chown=app:app ${JAR_LOCATION} /home/app/restapi.jar

EXPOSE 8443

# Run the application
CMD ["java", "-jar", "/home/app/restapi.jar"]