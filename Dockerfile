FROM maven:3.8.2-openjdk-11-slim AS MAVEN_BUILD

MAINTAINER Antonio Eloy

COPY pom.xml /build/
COPY src /build/src/

WORKDIR /build/
RUN mvn package

FROM openjdk:11-jre-slim

WORKDIR /app

COPY --from=MAVEN_BUILD /build/target/carteira-api-1.0.jar /app/

ENTRYPOINT ["java", "-jar", "carteira-api-1.0.jar"]