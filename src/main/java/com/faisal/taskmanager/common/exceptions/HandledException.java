package com.faisal.taskmanager.common.exceptions;

import lombok.Getter;

import java.util.List;

/**
 * Custom runtime exception for handling application-specific errors.
 *
 * <p>This exception supports both single error and multiple errors scenarios,
 * providing a flexible way to handle business logic errors throughout the application.</p>
 *
 * <p><b>Usage Examples:</b></p>
 * <pre>{@code
 * // Single error - entity not found
 * taskRepository.findById(id)
 *     .orElseThrow(() -> new HandledException(ErrorMessage.TASK_NOT_FOUND));
 *
 * // Single error with custom description
 * throw new HandledException(
 *     ErrorMessage.TASK_NOT_FOUND,
 *     "Task with ID " + taskId + " not found"
 * );
 *
 * // Multiple errors - field-level validation
 * List<ErrorDetail> errors = new ArrayList<>();
 * if (nameInvalid) {
 *     errors.add(ErrorDetail.builder()
 *         .internalCode(2001)
 *         .message("Task name is required")
 *         .field("name")
 *         .build());
 * }
 * if (dueDateInvalid) {
 *     errors.add(ErrorDetail.builder()
 *         .internalCode(2002)
 *         .message("Due date must be in the future")
 *         .field("dueDate")
 *         .build());
 * }
 * throw new HandledException(errors);
 * }</pre>
 *
 * @see ErrorMessage
 * @see ErrorDetail
 * @see ControllerExceptionHandler
 * @author Faisal
 */
@Getter
public class HandledException extends RuntimeException {

    private final ErrorMessage errorMessage;
    private final String description;
    private final List<ErrorDetail> errorDetails;

    /**
     * Constructs a new HandledException with the specified error message.
     * The error message will be used as the default description.
     *
     * @param errorMessage the predefined error message
     */
    public HandledException(final ErrorMessage errorMessage) {
        this(errorMessage, null, null);
    }

    /**
     * Constructs a new HandledException with the specified error message and custom description.
     *
     * @param errorMessage the predefined error message
     * @param description custom description (if null, error message will be used as default)
     */
    public HandledException(final ErrorMessage errorMessage, String description) {
        this(errorMessage, description, null);
    }

    /**
     * Constructs a new HandledException with the specified error message and cause.
     * The error message will be used as the default description.
     *
     * @param errorMessage the predefined error message
     * @param cause the underlying cause
     */
    public HandledException(final ErrorMessage errorMessage, Throwable cause) {
        this(errorMessage, null, cause);
    }

    /**
     * Constructs a new HandledException with the specified error message, custom description, and cause.
     *
     * @param errorMessage the predefined error message
     * @param description custom description (if null, error message will be used as default)
     * @param cause the underlying cause (can be null)
     */
    public HandledException(final ErrorMessage errorMessage, String description, Throwable cause) {
        super(cause);
        this.errorMessage = errorMessage;
        this.description = description;
        this.errorDetails = null;
    }

    /**
     * Constructs a new HandledException with a list of error details.
     * Use this constructor for multiple field-level errors.
     *
     * @param errorDetails list of error details
     */
    public HandledException(List<ErrorDetail> errorDetails) {
        super(errorDetails != null && !errorDetails.isEmpty()
            ? errorDetails.get(0).getMessage()
            : "Multiple errors occurred");
        this.errorMessage = null;
        this.description = null;
        this.errorDetails = errorDetails;
    }
}