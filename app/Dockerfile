ARG JAR_LOCATION=build/libs/restapi-all.jar
ARG BASE_IMAGE=azul/zulu-openjdk-alpine:21

FROM ${BASE_IMAGE}

COPY ${JAR_LOCATION} /app.jar

EXPOSE 8443

CMD ["-Xmx256m", "-jar", "/app.jar"]