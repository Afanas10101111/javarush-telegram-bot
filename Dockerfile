FROM ubuntu:22.10
ENV LANG=C.UTF-8
RUN apt-get update \
    && apt-get install -y --no-install-recommends openjdk-17-jre-headless=17.0.7+7~us1-0ubuntu1~22.10.2 \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*
ARG USER_NAME=jtb
ARG APP_PATH=/jtb-app
ARG PROJECT_PATH=/javarush-telegram-bot
WORKDIR $PROJECT_PATH
COPY . .
RUN useradd $USER_NAME \
	&& apt-get update \
	&& apt-get install -y --no-install-recommends maven=3.6.3-5ubuntu1.1 \
	&& mvn -Dmaven.test.skip clean package \
	&& mkdir $APP_PATH \
    && mv ./target/*.jar $APP_PATH/app.jar \
    && apt-get remove -y maven \
    && apt-get -y autoremove \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/* ~/.m2 $PROJECT_PATH
WORKDIR /$APP_PATH
USER $USER_NAME
ENTRYPOINT ["java","-jar","app.jar"]
