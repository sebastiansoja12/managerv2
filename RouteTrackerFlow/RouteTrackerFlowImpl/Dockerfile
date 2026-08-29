FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY . .
RUN cd RouteTrackerFlow && mvn -pl RouteTrackerFlowImpl -am package -DskipTests

FROM eclipse-temurin:21-jre

COPY --from=build /workspace/RouteTrackerFlow/RouteTrackerFlowImpl/target/RouteTrackerFlowImpl-2026.3-SNAPSHOT.jar /app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app.jar"]
