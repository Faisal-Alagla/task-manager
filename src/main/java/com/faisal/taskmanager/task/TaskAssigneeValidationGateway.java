package com.faisal.taskmanager.task;

import com.faisal.shared.messaging.api.IntegrationEventPublisher;
import com.faisal.shared.messaging.config.MessagingMetadata;
import com.faisal.shared.messaging.config.MessagingProperties;
import com.faisal.shared.messaging.model.DefaultIntegrationEvent;
import com.faisal.taskmanager.messaging.TaskAssigneeValidationEvents;
import com.faisal.taskmanager.messaging.inbound.TaskAssigneeValidationResultPayload;
import com.faisal.taskmanager.messaging.outbound.TaskAssigneeValidationRequestPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Coordinates the RabbitMQ request/reply flow used to validate a task assignee before task creation completes.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TaskAssigneeValidationGateway {

    private static final int EVENT_VERSION = 1;
    private static final String USER_NOT_FOUND_WARNING = "Assignee user was not found. Task created without assignee.";
    private static final String VALIDATION_UNAVAILABLE_WARNING =
            "Assignee could not be validated. Task created without assignee.";

    private final IntegrationEventPublisher integrationEventPublisher;
    private final MessagingProperties messagingProperties;
    private final TaskAssigneeValidationProperties taskAssigneeValidationProperties;
    private final MessagingMetadata messagingMetadata;
    // Per-JVM correlation map: replies must be delivered to the replica that issued the request. Scaling
    // task-manager to multiple replicas requires per-instance reply queues or a DB-backed correlation table.
    private final ConcurrentMap<String, CompletableFuture<TaskAssigneeValidationResultPayload>> pendingValidations =
            new ConcurrentHashMap<>();

    /**
     * Validates the requested assignee and converts the reply into the persistence decision used by task creation.
     *
     * @param assigneeId assignee id requested by the caller
     * @return normalized assignee resolution used by {@code TaskService#createTask}
     */
    public TaskAssigneeValidationOutcome validateAssignee(UUID assigneeId) {
        if (assigneeId == null) {
            return TaskAssigneeValidationOutcome.resolvedAssignee(null);
        }

        if (!messagingProperties.isEnabled()) {
            return TaskAssigneeValidationOutcome.withoutAssignee(VALIDATION_UNAVAILABLE_WARNING);
        }

        String correlationId = UUID.randomUUID().toString();
        CompletableFuture<TaskAssigneeValidationResultPayload> validationFuture = new CompletableFuture<>();
        pendingValidations.put(correlationId, validationFuture);

        try {
            integrationEventPublisher.publish(new DefaultIntegrationEvent<>(
                    TaskAssigneeValidationEvents.REQUEST_EVENT_TYPE,
                    EVENT_VERSION,
                    messagingMetadata.producerName(),
                    correlationId,
                    null,
                    assigneeId.toString(),
                    new TaskAssigneeValidationRequestPayload(assigneeId)
            ));

            TaskAssigneeValidationResultPayload validationResult = validationFuture.get(
                    taskAssigneeValidationProperties.getTimeoutMs(),
                    TimeUnit.MILLISECONDS
            );

            if (validationResult.userExists()) {
                return TaskAssigneeValidationOutcome.resolvedAssignee(assigneeId);
            }

            return TaskAssigneeValidationOutcome.withoutAssignee(USER_NOT_FOUND_WARNING);
        } catch (TimeoutException exception) {
            log.warn("Timed out waiting for assignee validation reply assigneeId={} correlationId={}",
                    assigneeId, correlationId);
            return TaskAssigneeValidationOutcome.withoutAssignee(VALIDATION_UNAVAILABLE_WARNING);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            log.warn("Interrupted while waiting for assignee validation reply assigneeId={} correlationId={}",
                    assigneeId, correlationId);
            return TaskAssigneeValidationOutcome.withoutAssignee(VALIDATION_UNAVAILABLE_WARNING);
        } catch (ExecutionException | RuntimeException exception) {
            log.error("Assignee validation request failed assigneeId={} correlationId={}",
                    assigneeId, correlationId, exception);
            return TaskAssigneeValidationOutcome.withoutAssignee(VALIDATION_UNAVAILABLE_WARNING);
        } finally {
            pendingValidations.remove(correlationId);
        }
    }

    /**
     * Completes the pending validation request identified by the supplied correlation id when the reply arrives.
     *
     * @param correlationId correlation id copied from the outbound validation request
     * @param payload reply payload containing the user-existence decision
     */
    public void completeValidation(String correlationId, TaskAssigneeValidationResultPayload payload) {
        if (correlationId == null || correlationId.isBlank()) {
            log.warn("Received assignee validation reply without correlation id");
            return;
        }

        CompletableFuture<TaskAssigneeValidationResultPayload> validationFuture = pendingValidations.get(correlationId);

        if (validationFuture == null) {
            log.warn("Received assignee validation reply with no pending request correlationId={}", correlationId);
            return;
        }

        if (payload == null) {
            validationFuture.completeExceptionally(
                    new IllegalStateException("Assignee validation reply payload is missing")
            );
            return;
        }

        validationFuture.complete(payload);
    }
}
