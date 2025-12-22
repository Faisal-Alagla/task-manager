package com.faisal.taskmanager.common.exceptions;

import lombok.Getter;

/**
 * Custom runtime exception for handling application-specific errors.
 *
 * <p>This exception wraps predefined error messages from the {@link ErrorMessage} enum,
 * providing a consistent way to handle business logic errors throughout the application.</p>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * // Throw when entity not found (uses default message as description)
 * taskRepository.findById(id)
 *     .orElseThrow(() -> new HandledException(ErrorMessage.TASK_NOT_FOUND));
 *
 * // Throw with custom description
 * throw new HandledException(
 *     ErrorMessage.TASK_NOT_FOUND,
 *     "Task with ID " + taskId + " not found"
 * );
 *
 * // Wrap another exception with custom description
 * try {
 *     externalService.call();
 * } catch (IOException e) {
 *     throw new HandledException(
 *         ErrorMessage.INTERNAL_SERVER_ERROR,
 *         "Failed to connect to external service",
 *         e
 *     );
 * }
 * }</pre>
 *
 * @see ErrorMessage
 * @see ControllerExceptionHandler
 * @author Faisal
 */
@Getter
public class HandledException extends RuntimeException {

    private final ErrorMessage errorMessage;
    private final String description;

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
    }
}