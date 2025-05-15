# Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the built JAR file
COPY target/api-gateway-0.0.1-SNAPSHOT.jar api-gateway-0.0.1-SNAPSHOT.jar

# Expose port for API Gateway
EXPOSE 8085

# Run the application
ENTRYPOINT ["java", "-jar", "api-gateway-0.0.1-SNAPSHOT.jar"]
