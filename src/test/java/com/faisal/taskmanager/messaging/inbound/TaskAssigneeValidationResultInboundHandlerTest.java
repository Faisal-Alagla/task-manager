package com.faisal.taskmanager.messaging.inbound;

import com.faisal.shared.messaging.model.EventEnvelope;
import com.faisal.taskmanager.messaging.TaskAssigneeValidationEvents;
import com.faisal.taskmanager.task.TaskAssigneeValidationGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static com.faisal.taskmanager.testutils.constants.TestConstants.USER_ID_1;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("TaskAssigneeValidationResultInboundHandler")
class TaskAssigneeValidationResultInboundHandlerTest {

    @Test
    @DisplayName("handle should complete the pending validation by correlation id")
    void handleShouldCompletePendingValidationByCorrelationId() {
        TaskAssigneeValidationGateway taskAssigneeValidationGateway = mock(TaskAssigneeValidationGateway.class);
        TaskAssigneeValidationResultInboundHandler handler =
                new TaskAssigneeValidationResultInboundHandler(taskAssigneeValidationGateway);
        TaskAssigneeValidationResultPayload payload = new TaskAssigneeValidationResultPayload(USER_ID_1, true);
        EventEnvelope<TaskAssigneeValidationResultPayload> event = new EventEnvelope<>(
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                TaskAssigneeValidationEvents.RESULT_EVENT_TYPE,
                1,
                Instant.parse("2026-04-23T10:00:00Z"),
                "user-manager",
                "corr-123",
                "cause-123",
                USER_ID_1.toString(),
                payload
        );

        handler.handle(event);

        verify(taskAssigneeValidationGateway).completeValidation("corr-123", payload);
    }
}
