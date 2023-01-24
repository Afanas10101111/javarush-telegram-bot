FROM adoptopenjdk/openjdk11:ubi
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Dbot.username=${BOT_NAME}","-Dbot.token=${BOT_TOKEN}","-Dbot.admin_names=${BOT_ADMIN_NAMES}","-Dbot.admin_chat_ids=${BOT_ADMIN_CHAT_IDS}","-Dspring.datasource.username=${DB_USERNAME}","-Dspring.datasource.password=${DB_PASSWORD}","-jar","/app.jar"]
