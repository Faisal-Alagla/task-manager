package com.faisal.taskmanager.messaging.inbound;

import com.faisal.shared.messaging.inbound.InboundEventHandler;
import com.faisal.shared.messaging.model.EventEnvelope;
import com.faisal.taskmanager.messaging.TaskAssigneeValidationEvents;
import com.faisal.taskmanager.task.TaskAssigneeValidationGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskAssigneeValidationResultInboundHandler implements InboundEventHandler<TaskAssigneeValidationResultPayload> {

    private final TaskAssigneeValidationGateway taskAssigneeValidationGateway;

    @Override
    public String eventType() {
        return TaskAssigneeValidationEvents.RESULT_EVENT_TYPE;
    }

    @Override
    public Class<TaskAssigneeValidationResultPayload> payloadType() {
        return TaskAssigneeValidationResultPayload.class;
    }

    @Override
    public void handle(EventEnvelope<TaskAssigneeValidationResultPayload> event) {
        taskAssigneeValidationGateway.completeValidation(event.correlationId(), event.payload());
    }
}
