package com.faisal.taskmanager.messaging.rabbit.topology;

import com.faisal.shared.messaging.config.MessagingProperties;
import com.faisal.shared.messaging.rabbit.topology.MessagingTopology;
import com.faisal.taskmanager.messaging.TaskAssigneeValidationEvents;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TaskManagerRabbitTopology")
class TaskManagerRabbitTopologyTest {

    @Test
    @DisplayName("should declare the assignee validation result queue, dlq, and bindings")
    void shouldDeclareAssigneeValidationResultQueueDlqAndBindings() {
        TaskManagerRabbitTopology topology = new TaskManagerRabbitTopology(new MessagingTopology(new MessagingProperties()));
        Declarables declarables = topology.taskManagerRabbitDeclarables();
        var allDeclarables = declarables.getDeclarables();

        assertThat(allDeclarables.stream()
                .filter(Queue.class::isInstance)
                .map(Queue.class::cast)
                .map(Queue::getName))
                .contains(
                        TaskManagerRabbitTopology.ASSIGNEE_VALIDATION_RESULT_QUEUE,
                        TaskManagerRabbitTopology.ASSIGNEE_VALIDATION_RESULT_DLQ
                );

        assertThat(allDeclarables.stream()
                .filter(Binding.class::isInstance)
                .map(Binding.class::cast)
                .anyMatch(binding ->
                        TaskManagerRabbitTopology.ASSIGNEE_VALIDATION_RESULT_QUEUE.equals(binding.getDestination())
                                && TaskAssigneeValidationEvents.RESULT_EVENT_TYPE.equals(binding.getRoutingKey())
                )).isTrue();

        assertThat(allDeclarables.stream()
                .filter(Binding.class::isInstance)
                .map(Binding.class::cast)
                .anyMatch(binding ->
                        TaskManagerRabbitTopology.ASSIGNEE_VALIDATION_RESULT_DLQ.equals(binding.getDestination())
                                && TaskManagerRabbitTopology.ASSIGNEE_VALIDATION_RESULT_DLQ_ROUTING_KEY
                                .equals(binding.getRoutingKey())
                )).isTrue();
    }
}
