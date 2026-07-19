# syntax=docker/dockerfile:1.7

FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY . .
RUN --mount=type=secret,id=maven_settings,target=/root/.m2/manager_settings.xml,required=false \
    cd RouteTrackerFlow && mvn -pl RouteTrackerFlowImpl -am package -DskipTests

FROM eclipse-temurin:21-jre

COPY --from=build /workspace/RouteTrackerFlow/RouteTrackerFlowImpl/target/RouteTrackerFlowImpl-2026.3-SNAPSHOT.jar /app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app.jar"]
