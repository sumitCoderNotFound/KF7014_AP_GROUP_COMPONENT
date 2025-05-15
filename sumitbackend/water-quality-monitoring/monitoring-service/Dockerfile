FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/monitoring-service-0.0.1-SNAPSHOT.jar monitoring-service-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "monitoring-service-0.0.1-SNAPSHOT.jar"]
