# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Copy the Spring Boot application
COPY  Application/target/Application-2023.8-SNAPSHOT.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Set the entry point to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app.jar"]
