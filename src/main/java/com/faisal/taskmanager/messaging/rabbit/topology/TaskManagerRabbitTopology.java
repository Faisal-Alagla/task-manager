package com.faisal.taskmanager.messaging.rabbit.topology;

import com.faisal.shared.messaging.rabbit.topology.MessagingTopology;
import com.faisal.taskmanager.messaging.TaskAssigneeValidationEvents;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Declarables;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ topology owned by task-manager.
 *
 * <p>Declares the assignee-validation reply queue (and its dead-letter queue) bound to the shared exchanges.
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "messaging", name = "enabled", havingValue = "true")
public class TaskManagerRabbitTopology {

    public static final String ASSIGNEE_VALIDATION_RESULT_QUEUE_BASE = "task-manager.assignee-validation-result";
    public static final String ASSIGNEE_VALIDATION_RESULT_QUEUE = ASSIGNEE_VALIDATION_RESULT_QUEUE_BASE + MessagingTopology.QUEUE_SUFFIX;
    public static final String ASSIGNEE_VALIDATION_RESULT_DLQ = ASSIGNEE_VALIDATION_RESULT_QUEUE_BASE + MessagingTopology.DLQ_SUFFIX;
    public static final String ASSIGNEE_VALIDATION_RESULT_DLQ_ROUTING_KEY = ASSIGNEE_VALIDATION_RESULT_DLQ;

    private final MessagingTopology messagingTopology;

    @Bean
    Declarables taskManagerRabbitDeclarables() {
        return messagingTopology.queueWithDlq(
                ASSIGNEE_VALIDATION_RESULT_QUEUE_BASE,
                TaskAssigneeValidationEvents.RESULT_EVENT_TYPE
        );
    }
}
