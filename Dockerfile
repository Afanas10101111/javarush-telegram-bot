FROM ubuntu:20.04
ENV LANG=C.UTF-8
RUN apt-get update \
    && apt-get install -y --no-install-recommends openjdk-11-jre-headless=11.0.18+10-0ubuntu1~20.04.1 \
    && apt-get install -y --no-install-recommends git=1:2.25.1-1ubuntu3.10 \
    && apt-get install -y --no-install-recommends maven=3.6.3-1 \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*
WORKDIR /app
ARG AUTHOR=Afanas10101111
ARG PROJECT=javarush-telegram-bot
ADD "https://api.github.com/repos/$AUTHOR/$PROJECT/commits?per_page=1" latest_commit
RUN git clone https://github.com/$AUTHOR/$PROJECT.git \
    && mvn -Dmaven.test.skip -f ./$PROJECT/pom.xml clean package \
    && mv ./$PROJECT/target/*.jar ./app.jar \
    && apt-get remove -y maven \
    && apt-get remove -y git \
    && apt-get -y autoclean \
    && apt-get -y autoremove \
    && rm -R ~/.m2 \
    && rm -R ./$PROJECT
ENTRYPOINT ["java","-jar","app.jar"]
