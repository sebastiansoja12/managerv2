FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY . .
RUN mvn -pl Application -am package -DskipTests

FROM eclipse-temurin:21-jre

COPY --from=build /workspace/Application/target/Application-2026.3-SNAPSHOT.jar /app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]
