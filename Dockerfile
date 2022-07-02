FROM openjdk:19-jdk-alpine3.15
ARG JAR_FILE=build/libs/QuotesRestAPI-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]