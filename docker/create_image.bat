@echo off
cd ..
IF "%~1"=="onlyDockerize" GOTO Dockerize

echo Cleaning and packaging app with maven image
call docker run -it --rm --name=och-maven -v "$PWD/:/usr/src/maven" -v "$PWD/maven-cache:/root/.m2" -v "$PWD/target:/usr/src/maven/target" -w /usr/src/maven maven:3.5.3-jdk-8 mvn clean package

:Dockerize
echo Time to build docker image for och
call docker build -f docker/Dockerfile -t hgmoa/oncontrolhome:latest .
echo Done