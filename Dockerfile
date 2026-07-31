# Step 1: Build phase using Maven & JDK 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

# Step 2: Runtime phase using JDK 21 JRE
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/payment-service-1.0.0-SNAPSHOT.jar app.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "app.jar"]
