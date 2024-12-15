FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/GoAdventure.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]