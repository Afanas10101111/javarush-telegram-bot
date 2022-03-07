#!/bin/bash

#git pull
mvn clean package
docker-compose stop

export BOT_NAME=$1
export BOT_TOKEN=$2
export BOT_ADMINS=$3
export DB_USERNAME=$4
export DB_PASSWORD=$5

docker-compose up --build -d
