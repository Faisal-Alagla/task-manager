package com.faisal.taskmanager.task.validator;

import com.faisal.taskmanager.common.exceptions.ErrorDetailBuilder;
import com.faisal.taskmanager.common.exceptions.ErrorMessage;
import com.faisal.taskmanager.task.TaskLookupContext;
import com.faisal.taskmanager.task.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Validator for Task-related business rules and constraints.
 *
 * <p>Handles validation logic for task operations, including existence checks
 * and field-level validations. Separated from the service layer for better
 * testability and maintainability.</p>
 *
 * @author Faisal
 */
@Component
@RequiredArgsConstructor
public class TaskValidator {

    // Field name constants
    private static final String FIELD_PARENT_TASK_ID = "parentTaskId";
    private static final String FIELD_STATUS_ID = "statusId";
    private static final String FIELD_PRIORITY_ID = "priorityId";

    private final TaskRepository taskRepository;
    private final TaskLookupContext taskLookupContext;

    /**
     * Validates all lookups for task creation
     *
     * @param statusId the status ID to validate
     * @param priorityId the priority ID to validate
     * @throws com.faisal.taskmanager.common.exceptions.HandledException if validation fails
     */
    public void validateTaskCreationLookups(Integer statusId, Integer priorityId) {
        new ErrorDetailBuilder()
                .addIf(!taskLookupContext.getTaskStatusIds().contains(statusId),
                        ErrorMessage.TASK_STATUS_NOT_FOUND,
                        FIELD_STATUS_ID)
                .addIf(!taskLookupContext.getTaskPriorityIds().contains(priorityId),
                        ErrorMessage.TASK_PRIORITY_NOT_FOUND,
                        FIELD_PRIORITY_ID)
                .throwIfHasErrors();
    }

    /**
     * Validates lookups for task update (all fields optional)
     *
     * @param statusId the status ID to validate (nullable)
     * @param priorityId the priority ID to validate (nullable)
     * @throws com.faisal.taskmanager.common.exceptions.HandledException if validation fails
     */
    public void validateTaskUpdateLookups(Integer statusId, Integer priorityId) {
        ErrorDetailBuilder builder = new ErrorDetailBuilder();

        if (statusId != null) {
            builder.addIf(!taskLookupContext.getTaskStatusIds().contains(statusId),
                    ErrorMessage.TASK_STATUS_NOT_FOUND,
                    FIELD_STATUS_ID);
        }

        if (priorityId != null) {
            builder.addIf(!taskLookupContext.getTaskPriorityIds().contains(priorityId),
                    ErrorMessage.TASK_PRIORITY_NOT_FOUND,
                    FIELD_PRIORITY_ID);
        }

        builder.throwIfHasErrors();
    }

    /**
     * Validates that a task exists in the system.
     *
     * @param taskId the task ID to validate
     * @param fieldName the field name to associate with the error (e.g., "taskId", "parentTaskId")
     * @throws com.faisal.taskmanager.common.exceptions.HandledException if task does not exist
     */
    public void validateTaskExists(UUID taskId, String fieldName) {
        if (taskId == null) {
            return; // Null checks handled by @NotNull or explicit business rules
        }

        boolean taskExists = taskRepository.findByIdAndIsActiveTrue(taskId).isPresent();

        new ErrorDetailBuilder()
                .addIf(!taskExists,
                        ErrorMessage.TASK_NOT_FOUND,
                        fieldName)
                .throwIfHasErrors();
    }

    /**
     * Validates that a parent task exists (nullable field).
     *
     * @param parentTaskId the parent task ID to validate (can be null)
     * @throws com.faisal.taskmanager.common.exceptions.HandledException if parent task does not exist
     */
    public void validateParentTaskExists(UUID parentTaskId) {
        validateTaskExists(parentTaskId, FIELD_PARENT_TASK_ID);
    }
}
