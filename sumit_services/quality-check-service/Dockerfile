FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/quality-check-service-0.0.1-SNAPSHOT.jar quality-check-service-0.0.1-SNAPSHOT.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "quality-check-service-0.0.1-SNAPSHOT.jar"]
