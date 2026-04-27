package com.faisal.taskmanager.task;

import java.util.UUID;

/**
 * Normalized assignee decision returned from assignee validation before a task is persisted.
 *
 * @param resolvedAssigneeId assignee id that should be saved on the task, or {@code null} when the task should be
 *                           created without an assignee
 * @param warning optional warning returned to the API caller when the requested assignee could not be kept
 */
public record TaskAssigneeValidationOutcome(
        UUID resolvedAssigneeId,
        String warning
) {

    /**
     * Creates a validation result that keeps the supplied assignee on the task.
     *
     * @param assigneeId assignee id to persist; may be {@code null} when the request had no assignee
     * @return validation result with the assignee preserved and no warning
     */
    public static TaskAssigneeValidationOutcome resolvedAssignee(UUID assigneeId) {
        return new TaskAssigneeValidationOutcome(assigneeId, null);
    }

    /**
     * Creates a validation result that removes the assignee from the task and returns a warning to the caller.
     *
     * @param warning warning explaining why the task will be created without an assignee
     * @return validation result with no assignee and the supplied warning
     */
    public static TaskAssigneeValidationOutcome withoutAssignee(String warning) {
        return new TaskAssigneeValidationOutcome(null, warning);
    }
}
