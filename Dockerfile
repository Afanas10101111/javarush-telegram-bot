FROM ubuntu:22.10
ENV LANG=C.UTF-8
ARG APP_PATH=/jtb-app
RUN mkdir $APP_PATH \
    && apt-get update \
    && apt-get install -y --no-install-recommends openjdk-17-jre-headless=17.0.6+10-0ubuntu1~22.10 \
    && apt-get install -y --no-install-recommends maven=3.6.3-5ubuntu1.1 \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*
ARG PROJECT_PATH=/javarush-telegram-bot
WORKDIR $PROJECT_PATH
COPY . .
RUN mvn -Dmaven.test.skip clean package \
    && mv ./target/*.jar $APP_PATH/app.jar \
    && apt-get remove -y maven \
    && apt-get -y autoremove \
    && rm -R ~/.m2 \
    && rm -R $PROJECT_PATH
WORKDIR /$APP_PATH
ENTRYPOINT ["java","-jar","app.jar"]
