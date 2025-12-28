package com.faisal.taskmanager.task;

import com.faisal.taskmanager.common.exceptions.ErrorDetailBuilder;
import com.faisal.taskmanager.common.exceptions.ErrorMessage;
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

    private final TaskRepository taskRepository;

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
                .addIf(!taskExists, ErrorMessage.TASK_NOT_FOUND, fieldName)
                .throwIfHasErrors();
    }

    /**
     * Validates that a parent task exists (nullable field).
     *
     * @param parentTaskId the parent task ID to validate (can be null)
     * @throws com.faisal.taskmanager.common.exceptions.HandledException if parent task does not exist
     */
    public void validateParentTaskExists(UUID parentTaskId) {
        validateTaskExists(parentTaskId, "parentTaskId");
    }
}
