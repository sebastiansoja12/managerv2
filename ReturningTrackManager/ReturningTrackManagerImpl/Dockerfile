# syntax=docker/dockerfile:1.7

FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY . .
RUN --mount=type=secret,id=maven_settings,target=/root/.m2/manager_settings.xml,required=false \
    cd ReturningTrackManager && mvn -pl ReturningTrackManagerImpl -am package -DskipTests

FROM eclipse-temurin:21-jre

COPY --from=build /workspace/ReturningTrackManager/ReturningTrackManagerImpl/target/ReturningTrackManagerImpl-2026.3-SNAPSHOT.jar /app.jar

EXPOSE 8070

ENTRYPOINT ["java", "-jar", "/app.jar"]
