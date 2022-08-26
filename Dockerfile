FROM gradle:jdk11-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:11-jre-slim

EXPOSE 8080

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/ToDoAPI-1.0.0.jar /app/ToDo-application.jar

ENTRYPOINT ["java", "-jar", "/app/ToDo-application.jar"]
