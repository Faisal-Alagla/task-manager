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
 * // Throw when entity not found
 * taskRepository.findById(id)
 *     .orElseThrow(() -> new HandledException(ErrorMessage.TASK_NOT_FOUND));
 *
 * // Wrap another exception
 * try {
 *     externalService.call();
 * } catch (IOException e) {
 *     throw new HandledException(ErrorMessage.INTERNAL_SERVER_ERROR, e);
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

    /**
     * Constructs a new HandledException with the specified error message.
     *
     * @param errorMessage the predefined error message
     */
    public HandledException(final ErrorMessage errorMessage) {
        this(errorMessage, null);
    }

    /**
     * Constructs a new HandledException with the specified error message and cause.
     *
     * @param errorMessage the predefined error message
     * @param cause the underlying cause (can be null)
     */
    public HandledException(final ErrorMessage errorMessage, Throwable cause) {
        super(cause);
        this.errorMessage = errorMessage;
    }
}