FROM openjdk:19-jdk-slim
LABEL authors="Bohdan_Sh"

WORKDIR /app

COPY target/train-up-0.0.1-SNAPSHOT.jar app.jar

EXPOSE ${PORT:-10000}

ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=${PORT:-10000}"]
