# Stage 1: Build application with Maven
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# Copy parent pom and module poms for efficient caching
COPY pom.xml ./pom.xml
COPY order-service/pom.xml ./order-service/pom.xml
COPY order-service/src ./order-service/src

# Build order-service module artifact
RUN mvn clean package -pl order-service -am -DskipTests

# Stage 2: Minimal Runtime Environment
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create non-root app user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy jar from build stage
COPY --from=builder /app/order-service/target/*.jar app.jar

# Set permissions
RUN chown -R appuser:appgroup /app
USER appuser

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
