#!/bin/bash

export BOT_NAME=$1
export BOT_TOKEN=$2
export BOT_ADMIN_NAMES=$3
export BOT_ADMIN_CHAT_IDS=$4
export DB_USERNAME=$5
export DB_PASSWORD=$6

docker-compose up --build -d
