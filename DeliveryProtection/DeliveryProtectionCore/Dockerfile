FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY . .
RUN mvn -pl Common,ExceptionHandler,ManagerTools/Properties -am install -DskipTests
RUN cd DeliveryProtection && mvn -pl DeliveryProtectionCore -am package spring-boot:repackage -DskipTests

FROM eclipse-temurin:21-jre

COPY --from=build /workspace/DeliveryProtection/DeliveryProtectionCore/target/DeliveryProtectionCore-2026.3-SNAPSHOT.jar /app.jar

EXPOSE 8060

ENTRYPOINT ["java", "-jar", "/app.jar"]
