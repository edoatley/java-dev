# https://learn.microsoft.com/en-us/java/openjdk/containers
ARG BASE_IMAGE=azul/zulu-openjdk-alpine:21
FROM ${BASE_IMAGE}

# Important scope the ARG after the FROM!!!
ARG JAR_FILE_LOCN=build/libs/restapi-all.jar

# Non-root user for running the application
RUN addgroup -S app && adduser -S app -G app
USER app:app

# No need to run software as privileged
RUN chmod 755 /home/app

# Copy the jar file
COPY --chown=app:app ${JAR_FILE_LOCN} /home/app/restapi.jar

EXPOSE 8443

# Run the application
CMD ["java", "-jar", "/home/app/restapi.jar"]