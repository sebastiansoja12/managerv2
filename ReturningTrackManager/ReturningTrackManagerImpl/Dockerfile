FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY . .
RUN cd ReturningTrackManager && mvn -pl ReturningTrackManagerImpl -am package -DskipTests

FROM eclipse-temurin:21-jre

COPY --from=build /workspace/ReturningTrackManager/ReturningTrackManagerImpl/target/ReturningTrackManagerImpl-2026.3-SNAPSHOT.jar /app.jar

EXPOSE 8070

ENTRYPOINT ["java", "-jar", "/app.jar"]
