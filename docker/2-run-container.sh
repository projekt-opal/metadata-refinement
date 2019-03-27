#!/bin/bash

# This file builds and runs a docker container based on a docker image.
# It publishes a container port to a host port.



# Uncomment for disabling script (as it has to be executed only one time)

#echo "Script disabled." ; exit



# Configuration

CONTAINER_NAME="opal-metadata-extraction"
PORT_HOST="4444"

# Image source for the container to create
IMAGE_NAME="opal-metadata-extraction"
TAG_NAME="latest"

# Static Container configuration
PORT_CONTAINER="4444"



# Ensure correct working directory

cd "$(dirname "$0")"



# Execution

docker run \
-t -d \
-p $PORT_HOST:$PORT_CONTAINER \
--name $CONTAINER_NAME \
$IMAGE_NAME:$TAG_NAME
