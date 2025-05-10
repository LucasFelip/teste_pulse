FROM ubuntu:latest
LABEL authors="lucas"
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
COPY src ./src
RUN mvn clean package -DskipTests
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/teste_pulse-0.0.1-SNAPSHOT.jar app.jar
EXPOSE ${SERVER_PORT}
ENTRYPOINT ["java", "-jar", "app.jar"]