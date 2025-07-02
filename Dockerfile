# Stage 1: Build the application using Maven
FROM maven:3.8.6-openjdk-17 AS builder

WORKDIR /app

# Copy the source code into the container
COPY . .

# Build the app (skip tests to speed up build)
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose port (optional; change if your app runs on a different port)
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
