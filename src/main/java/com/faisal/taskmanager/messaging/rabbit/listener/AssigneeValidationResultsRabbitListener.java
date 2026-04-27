package com.faisal.taskmanager.messaging.rabbit.listener;

import com.faisal.shared.messaging.rabbit.listener.RabbitInboundEventDispatcher;
import com.faisal.taskmanager.messaging.rabbit.topology.TaskManagerRabbitTopology;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "messaging", name = "enabled", havingValue = "true")
public class AssigneeValidationResultsRabbitListener {

    private final RabbitInboundEventDispatcher rabbitInboundEventDispatcher;

    @RabbitListener(
            queues = TaskManagerRabbitTopology.ASSIGNEE_VALIDATION_RESULT_QUEUE,
            containerFactory = "messagingRabbitListenerContainerFactory"
    )
    public void onAssigneeValidationResult(Message message) {
        rabbitInboundEventDispatcher.dispatch(message);
    }
}
