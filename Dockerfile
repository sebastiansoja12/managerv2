FROM eclipse-temurin:21-jre

ARG JAR_FILE=Application/target/Application-2026.3-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]
