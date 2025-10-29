# Build stage
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and download dependencies (cached layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests -B

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Install curl for healthcheck
RUN apk add --no-cache curl

# Create non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy jar from build stage
COPY --from=build /app/target/puente-app-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# JVM optimizations for containerized environments
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=50.0"

# Run application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]