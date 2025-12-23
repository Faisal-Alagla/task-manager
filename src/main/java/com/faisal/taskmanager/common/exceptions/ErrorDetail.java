package com.faisal.taskmanager.common.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a single error detail in an error response.
 *
 * <p>Each error contains an internal code, message, and optional field and description.
 * Multiple ErrorDetail objects can be included in a single {@link ErrorResponse}.</p>
 *
 * <p><b>Usage Examples:</b></p>
 * <pre>{@code
 * // Field-level error
 * ErrorDetail error = ErrorDetail.builder()
 *     .internalCode(2001)
 *     .message("Task name is required")
 *     .field("name")
 *     .build();
 *
 * // General error
 * ErrorDetail error = ErrorDetail.builder()
 *     .internalCode(2000)
 *     .message("Task not found")
 *     .description("No task found with ID abc-123")
 *     .build();
 * }</pre>
 *
 * @see ErrorResponse
 * @author Faisal
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDetail {

    /**
     * Internal error code for programmatic error handling.
     */
    private Integer internalCode;

    /**
     * Human-readable error message.
     */
    private String message;

    /**
     * Optional field name associated with this error (for field-level validation errors).
     */
    private String field;

    /**
     * Optional additional description or context for the error.
     */
    private String description;
}
