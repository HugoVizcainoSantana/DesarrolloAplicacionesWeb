#!/bin/sh
echo "Starting image..."
docker run -p 8443:8443 -it --name=och hgmoa/oncontrolhome:latest