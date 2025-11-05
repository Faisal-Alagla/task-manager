#Stage 1

FROM gradle:8.11.1-jdk17 AS builder

WORKDIR /app

# Copy build files
COPY settings.gradle .
COPY build.gradle .

# Download dependencies (layer caching)
RUN gradle dependencies --no-daemon

# Copy source code
COPY ./src ./src

# Build the application
RUN gradle clean build -x test --no-daemon

#Stage 2

FROM eclipse-temurin:17.0.7_7-jre-jammy

WORKDIR /app

COPY --from=builder /app/build/libs/task-manager-0.0.1-SNAPSHOT.jar /app

ENTRYPOINT ["java", "-jar", "/app/task-manager-0.0.1-SNAPSHOT.jar"]