# JAVA 8 as BASE
FROM openjdk:8-jdk-alpine

# Working directory for docker image
WORKDIR /app

# Folder inside alpine
COPY ./docker/spring-0.0.3-DAW.jar app.jar

# Expose PORT
EXPOSE 8443

# Start image
ENTRYPOINT ["java", "-jar", "app.jar"]