@echo off
echo "Cleaning folder and packaging app"
DEL "*.jar"
cd ..
call mvnw clean package
echo "Time to build docker image"
call docker build -f docker/Dockerfile -t hgmoa/oncontrolhome:latest .
echo "Done"