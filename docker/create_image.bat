@echo off
cd ..
IF "%~1"=="onlyDocker" GOTO Dockerize

echo "Cleaning folder and packaging app"
DEL "*.jar"
call mvnw clean package

:Dockerize
echo "Time to build docker image"
call docker build -f docker/Dockerfile -t hgmoa/oncontrolhome:latest .
echo "Done"