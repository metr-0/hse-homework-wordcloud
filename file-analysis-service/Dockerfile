FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests



FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=build /app/target/file-analysis-service-*.jar app.jar

RUN mkdir -p /app/files
EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
