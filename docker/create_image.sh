#!/bin/sh

cd ..
echo "Cleaning and packaging app with maven image"
docker run -it --rm --name=och-maven -v "$PWD:/usr/src/maven" -v "$PWD/maven-cache:/root/.m2" -v "$PWD/target:/usr/src/maven/target" -w /usr/src/maven maven:3.5.3-jdk-8 mvn package -Dmaven.test.skip=true

echo "Time to build docker image for och"
docker build -f docker/Dockerfile -t hgmoa/oncontrolhome:latest .
echo "Done"