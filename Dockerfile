FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /unitrack

COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src

RUN mvn clean package -DskipTests -Pdocker

FROM amazoncorretto:21

WORKDIR /unitrack

COPY --from=build /unitrack/target/unitrack-0.0.1-SNAPSHOT.jar /unitrack.jar

EXPOSE 2017

ENTRYPOINT ["java", "-jar", "/unitrack.jar"]
