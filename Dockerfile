#Stage 1

FROM gradle:8.11.1-jdk21 AS builder

WORKDIR /app

# Copy build files
COPY settings.gradle .
COPY build.gradle .

# Download dependencies (layer caching)
RUN gradle dependencies --no-daemon

# Copy source code
COPY ./src ./src

# Build the application (includes tests)
RUN gradle clean build --no-daemon

#Stage 2

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder /app/build/libs/task-manager-0.0.1-SNAPSHOT.jar /app

ENTRYPOINT ["java", "-jar", "/app/task-manager-0.0.1-SNAPSHOT.jar"]