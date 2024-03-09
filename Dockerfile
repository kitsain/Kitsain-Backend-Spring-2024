# Docker Build Stage
FROM maven:3.8.3-openjdk-17 AS build


# Build Stage
WORKDIR /opt/app

COPY ./ /opt/app
RUN --mount=type=cache,target=/root/.m2 mvn clean install -DskipTests
#RUN mvn clean install -DskipTests

# Docker Build Stage
FROM openjdk:17-alpine

COPY --from=build /opt/app/target/*.jar app.jar

ENV PORT 8080
EXPOSE $PORT

ENTRYPOINT ["java","-jar","-Xmx1024M","-Dserver.port=${PORT}","app.jar","--spring.profiles.active=prod"]
