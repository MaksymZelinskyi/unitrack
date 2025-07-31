FROM maven AS build

WORKDIR /unitrack

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM amazoncorretto:21

WORKDIR /unitrack

COPY --from=build /unitrack/target/unitrack-0.0.1-SNAPSHOT.jar /unitrack.jar

EXPOSE 2017

ENTRYPOINT ["java", "-jar", "/unitrack.jar"]
