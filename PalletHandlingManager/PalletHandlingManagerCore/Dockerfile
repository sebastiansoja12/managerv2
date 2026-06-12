FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY . .
RUN mvn -pl Common -am install -DskipTests
RUN cd PalletHandlingManager && mvn -pl PalletHandlingManagerCore -am package spring-boot:repackage -DskipTests

FROM eclipse-temurin:21-jre

COPY --from=build /workspace/PalletHandlingManager/PalletHandlingManagerCore/target/PalletHandlingManagerCore-2026.3-SNAPSHOT.jar /app.jar

EXPOSE 8071

ENTRYPOINT ["java", "-jar", "/app.jar"]
