#!/bin/bash

#git pull
mvn clean package
docker-compose stop

export BOT_NAME=$1
export BOT_TOKEN=$2
export DB_USERNAME=$3
export DB_PASSWORD=$4

docker-compose up --build -d
