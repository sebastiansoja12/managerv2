# syntax=docker/dockerfile:1.7

FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /workspace

ARG MAVEN_WORKDIR=.
ARG MAVEN_PROJECT=Application
ARG MAVEN_ARGS="-DskipTests"

COPY . .
RUN --mount=type=secret,id=maven_settings,target=/root/.m2/settings.xml,required=false \
    cd "${MAVEN_WORKDIR}" && mvn -pl "${MAVEN_PROJECT}" -am package ${MAVEN_ARGS}

FROM eclipse-temurin:21-jre

ARG JAR_FILE=Application/target/Application-2026.3-SNAPSHOT.jar
COPY --from=build /workspace/${JAR_FILE} /app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]
