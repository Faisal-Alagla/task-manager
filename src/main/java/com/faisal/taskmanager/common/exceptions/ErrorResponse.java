package com.faisal.taskmanager.common.exceptions;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * Standardized error response structure for all API errors.
 *
 * <p>Provides a consistent format for error responses across the application:</p>
 * <pre>{@code
 * {
 *   "timestamp": "2025-10-29T10:30:00.000+00:00",
 *   "internalCode": 5000,
 *   "message": "Task not found",
 *   "description": "Additional context (optional)"
 * }
 * }</pre>
 *
 * @see ErrorMessage
 * @see HandledException
 * @author Faisal
 */
@Data
@Builder
public class ErrorResponse {
    private Date timestamp;
    private Integer internalCode;
    private String message;
    private String description;
}