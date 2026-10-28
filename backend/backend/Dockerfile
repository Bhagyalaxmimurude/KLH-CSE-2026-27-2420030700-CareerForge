
# Stage 1: Build the Spring Boot application
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN apt-get update \
    && apt-get install -y --no-install-recommends maven \
    && mvn clean package -DskipTests \
    && rm -rf /var/lib/apt/lists/*

# Stage 2: Run the application using a lightweight Java 21 image
FROM amazoncorretto:21-alpine

WORKDIR /app

COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
