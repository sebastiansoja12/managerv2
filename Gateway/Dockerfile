FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY . .
RUN mvn -pl Gateway -am package -DskipTests

FROM eclipse-temurin:21-jre

COPY --from=build /workspace/Gateway/target/Gateway-2026.3-SNAPSHOT.jar /app.jar

EXPOSE 8088

ENTRYPOINT ["java", "-jar", "/app.jar"]
