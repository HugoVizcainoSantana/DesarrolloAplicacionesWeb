# JAVA 8 as BASE
FROM openjdk:8-jdk-alpine

# Folder inside alpine
COPY ./docker/spring-0.0.3-DAW.jar /usr/app/app.jar

# Mount volume for app
VOLUME /Users/och:/usr/app

# Expose PORT
EXPOSE 8443

# Start image
ENTRYPOINT ["java", "-jar", "/usr/app/app.jar"]