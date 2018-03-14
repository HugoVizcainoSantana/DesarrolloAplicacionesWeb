@echo off
echo Publishing image
cd ..
call docker push hgmoa/oncontrolhome:latest
echo Done