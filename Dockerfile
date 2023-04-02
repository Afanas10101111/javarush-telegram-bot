FROM ubuntu:20.04
ENV LANG=C.UTF-8
RUN apt-get update && \
    apt-get install -y openjdk-11-jre-headless && \
    apt-get install -y git && \
    apt-get install -y maven
WORKDIR /app
ARG AUTHOR=Afanas10101111
ARG PROJECT=javarush-telegram-bot
ADD "https://api.github.com/repos/$AUTHOR/$PROJECT/commits?per_page=1" latest_commit
RUN git clone https://github.com/$AUTHOR/$PROJECT.git && \
    cd ./$PROJECT && \
    mvn clean package && \
    mv ./target/*.jar /app/app.jar && \
    apt-get remove -y maven && \
    apt-get remove -y git && \
    apt-get -y autoclean && \
    apt-get -y autoremove && \
    rm -R ~/.m2 && \
    rm -R /app/$PROJECT
ENTRYPOINT ["java","-jar","app.jar"]
