# Stage 1: Build the application using Gradle
FROM gradle:8.7-jdk21 AS builder

# Copy everything and build the app
WORKDIR /home/gradle/project
COPY --chown=gradle:gradle . .

# Build the app (using the bootJar task)
RUN gradle bootJar --no-daemon

# Stage 2: Run the application using a minimal JDK runtime
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Copy the fat JAR from the builder stage
COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar

# Expose the port (matching server.port=9081)
EXPOSE 9081

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
