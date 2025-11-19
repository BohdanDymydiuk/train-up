FROM eclipse-temurin:21-jdk-jammy
LABEL authors="Bohdan_Sh"

WORKDIR /app

COPY target/train-up-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
