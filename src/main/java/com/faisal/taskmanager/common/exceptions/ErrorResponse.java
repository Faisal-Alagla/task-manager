package com.faisal.taskmanager.common.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Standardized error response structure for the application.
 *
 * <p>Contains a timestamp and a list of {@link ErrorDetail} objects.
 * This structure allows returning multiple errors in a single response.</p>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * ErrorResponse response = ErrorResponse.builder()
 *     .timestamp(new Date())
 *     .errors(List.of(
 *         ErrorDetail.builder()
 *             .internalCode(2000)
 *             .message("Task not found")
 *             .description("No task found with ID abc-123")
 *             .build()
 *     ))
 *     .build();
 * }</pre>
 *
 * @see ErrorDetail
 * @see HandledException
 * @author Faisal
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    /**
     * Timestamp when the error occurred.
     */
    private Date timestamp;

    /**
     * List of error details. Can contain one or more errors.
     */
    private List<ErrorDetail> errors;

}