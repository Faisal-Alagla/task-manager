package com.faisal.taskmanager.messaging.inbound;

import java.util.UUID;

public record TaskAssigneeValidationResultPayload(
        UUID assigneeId,
        boolean userExists
) {
}
