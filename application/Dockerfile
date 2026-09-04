# Stage 1: Build JAR using Maven Wrapper
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /workspace

RUN apk add --no-cache bash

# Copy Maven wrapper files and configuration
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x mvnw

# Cache Maven dependencies
RUN ./mvnw dependency:go-offline -B || true

# Copy source code and build artifact
COPY src src
RUN ./mvnw clean package -DskipTests

# Stage 2: Lightweight JRE runtime container
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Run as non-root user for container security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

COPY --from=builder /workspace/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

