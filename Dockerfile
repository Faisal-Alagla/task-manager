#Stage 1

FROM gradle:8.11.1-jdk21 AS builder

WORKDIR /app

# Build and publish shared-messaging into the in-image mavenLocal so the service can resolve it.
COPY libraries/shared-messaging libraries/shared-messaging
RUN cd libraries/shared-messaging && gradle publishToMavenLocal --no-daemon

# Copy this service and build it.
COPY services/task-manager services/task-manager
WORKDIR /app/services/task-manager

RUN gradle clean build --no-daemon

#Stage 2

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder /app/services/task-manager/build/libs/task-manager-0.0.1-SNAPSHOT.jar /app

ENTRYPOINT ["java", "-jar", "/app/task-manager-0.0.1-SNAPSHOT.jar"]
