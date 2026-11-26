FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY . .
RUN mvn -f EventOutbox/pom.xml install -DskipTests \
    && cd ReturningTrackManager && mvn -pl ReturningTrackManagerImpl -am package -DskipTests

FROM eclipse-temurin:21-jre

COPY --from=build /workspace/ReturningTrackManager/ReturningTrackManagerImpl/target/ReturningTrackManagerImpl-1.0.0-RC-SNAPSHOT.jar /app.jar

EXPOSE 8070

ENTRYPOINT ["java", "-jar", "/app.jar"]
