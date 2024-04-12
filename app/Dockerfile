# Use a minimal base image
FROM azul/zulu-openjdk-alpine:21

# Non-root user for running the application
RUN addgroup -S app && adduser -S app -G app
USER app:app

# No need to run software as privileged
RUN chmod 755 /home/app

# Copy the jar file
COPY --chown=app:app build/libs/restapi-all.jar /home/app/restapi.jar

# Run the application
CMD ["java", "-jar", "/home/app/restapi.jar"]