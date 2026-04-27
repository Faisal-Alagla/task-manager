package com.faisal.taskmanager.task;

import com.faisal.shared.messaging.api.IntegrationEventPublisher;
import com.faisal.shared.messaging.config.MessagingMetadata;
import com.faisal.shared.messaging.config.MessagingProperties;
import com.faisal.shared.messaging.model.IntegrationEvent;
import com.faisal.taskmanager.messaging.inbound.TaskAssigneeValidationResultPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.faisal.taskmanager.testutils.constants.TestConstants.USER_ID_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
@DisplayName("TaskAssigneeValidationGateway")
class TaskAssigneeValidationGatewayTest {

    private static final String PRODUCER_NAME = "task-manager";

    @Mock
    private IntegrationEventPublisher integrationEventPublisher;

    private MessagingProperties messagingProperties;
    private TaskAssigneeValidationProperties taskAssigneeValidationProperties;
    private TaskAssigneeValidationGateway taskAssigneeValidationGateway;

    @BeforeEach
    void setUp() {
        messagingProperties = new MessagingProperties();
        messagingProperties.setEnabled(true);
        taskAssigneeValidationProperties = new TaskAssigneeValidationProperties();
        taskAssigneeValidationProperties.setTimeoutMs(20);
        taskAssigneeValidationGateway = new TaskAssigneeValidationGateway(
                integrationEventPublisher,
                messagingProperties,
                taskAssigneeValidationProperties,
                new MessagingMetadata(PRODUCER_NAME)
        );
    }

    @Test
    @DisplayName("validateAssignee should keep assignee when the reply confirms the user exists")
    void validateAssigneeShouldKeepAssigneeWhenUserExists() {
        doAnswer(invocation -> {
            IntegrationEvent<?> event = invocation.getArgument(0);
            taskAssigneeValidationGateway.completeValidation(
                    event.correlationId(),
                    new TaskAssigneeValidationResultPayload(USER_ID_1, true)
            );
            return null;
        }).when(integrationEventPublisher).publish(any());

        TaskAssigneeValidationOutcome result = taskAssigneeValidationGateway.validateAssignee(USER_ID_1);

        assertThat(result.resolvedAssigneeId()).isEqualTo(USER_ID_1);
        assertThat(result.warning()).isNull();
        ArgumentCaptor<IntegrationEvent> integrationEventCaptor = ArgumentCaptor.forClass(IntegrationEvent.class);
        verify(integrationEventPublisher).publish(integrationEventCaptor.capture());
        assertThat(integrationEventCaptor.getValue().producer()).isEqualTo(PRODUCER_NAME);
    }

    @Test
    @DisplayName("validateAssignee should clear assignee when the reply says the user is missing")
    void validateAssigneeShouldClearAssigneeWhenUserIsMissing() {
        doAnswer(invocation -> {
            IntegrationEvent<?> event = invocation.getArgument(0);
            taskAssigneeValidationGateway.completeValidation(
                    event.correlationId(),
                    new TaskAssigneeValidationResultPayload(USER_ID_1, false)
            );
            return null;
        }).when(integrationEventPublisher).publish(any());

        TaskAssigneeValidationOutcome result = taskAssigneeValidationGateway.validateAssignee(USER_ID_1);

        assertThat(result.resolvedAssigneeId()).isNull();
        assertThat(result.warning()).isEqualTo("Assignee user was not found. Task created without assignee.");
    }

    @Test
    @DisplayName("validateAssignee should clear assignee immediately when messaging is disabled")
    void validateAssigneeShouldClearAssigneeWhenMessagingIsDisabled() {
        messagingProperties.setEnabled(false);

        TaskAssigneeValidationOutcome result = taskAssigneeValidationGateway.validateAssignee(USER_ID_1);

        assertThat(result.resolvedAssigneeId()).isNull();
        assertThat(result.warning()).isEqualTo("Assignee could not be validated. Task created without assignee.");
        verifyNoInteractions(integrationEventPublisher);
    }

    @Test
    @DisplayName("validateAssignee should clear assignee when the reply times out")
    void validateAssigneeShouldClearAssigneeWhenReplyTimesOut() {
        TaskAssigneeValidationOutcome result = taskAssigneeValidationGateway.validateAssignee(USER_ID_1);

        assertThat(result.resolvedAssigneeId()).isNull();
        assertThat(result.warning()).isEqualTo("Assignee could not be validated. Task created without assignee.");
    }
}
