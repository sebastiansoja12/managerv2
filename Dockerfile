# Use an official OpenJDK runtime as a parent image
FROM openjdk:21

# Copy the Spring Boot application
COPY  Application/target/Application-2025.1-SNAPSHOT.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Set the entry point to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app.jar"]
