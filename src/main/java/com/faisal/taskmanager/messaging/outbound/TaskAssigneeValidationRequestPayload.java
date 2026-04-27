package com.faisal.taskmanager.messaging.outbound;

import java.util.UUID;

public record TaskAssigneeValidationRequestPayload(
        UUID assigneeId
) {
}
