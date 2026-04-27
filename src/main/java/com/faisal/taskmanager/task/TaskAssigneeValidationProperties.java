package com.faisal.taskmanager.task;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Service-specific properties for the task-manager assignee validation request/reply flow.
 *
 * <p>Lives in task-manager (not in shared messaging) because the timeout is a feature-level knob, not a generic
 * messaging-infrastructure setting.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "task-manager.assignee-validation")
public class TaskAssigneeValidationProperties {

    /**
     * Maximum time the assignee-validation requester waits for a reply before falling back.
     */
    private long timeoutMs;
}
