FROM maven AS build

WORKDIR /univr

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM amazoncorretto:21

WORKDIR /univr

COPY --from=build /univr/target/unitrack-0.0.1-SNAPSHOT.jar /unitrack.jar

EXPOSE 2017

ENTRYPOINT ["java", "-jar", "/unitrack.jar"]
