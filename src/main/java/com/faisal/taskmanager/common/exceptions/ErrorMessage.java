package com.faisal.taskmanager.common.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Enumeration of all application-specific error messages.
 *
 * <p>Each error contains an internal code, HTTP status, and user-friendly message.
 * Internal codes are organized by category:</p>
 * <ul>
 *   <li>1xxx - General errors</li>
 *   <li>2xxx - Validation errors</li>
 *   <li>3xxx - Data integrity errors</li>
 *   <li>4xxx - Business logic errors</li>
 *   <li>5xxx - Task domain errors</li>
 *   <li>6xxx - Issue domain errors</li>
 * </ul>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * throw new HandledException(ErrorMessage.TASK_NOT_FOUND);
 * }</pre>
 *
 * @see HandledException
 * @see ErrorResponse
 * @author Faisal
 */
@Getter
public enum ErrorMessage {

    // General errors (1xxx)
    INTERNAL_SERVER_ERROR(1000, HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    RESOURCE_NOT_FOUND_ERROR(1001, HttpStatus.NOT_FOUND, "Resource not found"),

    // Validation errors (2xxx)
    CONSTRAINT_VIOLATED_ERROR(2000, HttpStatus.BAD_REQUEST, "Constraint violated error"),
    INVALID_REQUEST_PAYLOAD(2001, HttpStatus.BAD_REQUEST, "The request content is not valid and could not be deserialized"),
    INVALID_REQUEST_ATTRIBUTES(2002, HttpStatus.BAD_REQUEST, "Invalid request attributes"),
    INVALID_PATH(2003, HttpStatus.BAD_REQUEST, "Invalid path value"),
    MISSING_REQUEST_PARAMETER(2004, HttpStatus.BAD_REQUEST, "Required parameter is missing"),
    METHOD_ARGUMENT_MISMATCH_ERROR(2005, HttpStatus.BAD_REQUEST, "Method argument type mismatch"),
    HTTP_MESSAGE_NOT_READABLE_ERROR(2006, HttpStatus.BAD_REQUEST, "HTTP message not readable"),
    UNSUPPORTED_REQUEST_METHOD(2007, HttpStatus.METHOD_NOT_ALLOWED, "Request method is not supported"),
    INVALID_API_USAGE(2008, HttpStatus.BAD_REQUEST, "Invalid API usage"),

    // Data integrity errors (3xxx)
    DATA_INTEGRITY_VIOLATION_ERROR(3000, HttpStatus.CONFLICT, "Data integrity violation error"),

    // Business logic errors (4xxx)
    INVALID_STATUS_TRANSITION(4000, HttpStatus.BAD_REQUEST, "Invalid status transition"),

    // Task domain errors (5xxx)
    TASK_NOT_FOUND(5000, HttpStatus.NOT_FOUND, "Task not found"),
    TASK_PRIORITY_NOT_FOUND(5001, HttpStatus.NOT_FOUND, "Task priority not found"),
    TASK_STATUS_NOT_FOUND(5002, HttpStatus.NOT_FOUND, "Task status not found"),

    // Issue domain errors (6xxx)
    ISSUE_NOT_FOUND(6000, HttpStatus.NOT_FOUND, "Issue not found"),
    ISSUE_CRITICALITY_NOT_FOUND(6001, HttpStatus.NOT_FOUND, "Issue criticality not found"),
    ISSUE_STATUS_NOT_FOUND(6002, HttpStatus.NOT_FOUND, "Issue status not found");

    private final int internalCode;
    private final HttpStatus httpStatus;
    private final String message;

    ErrorMessage(int internalCode, HttpStatus httpStatus, String message) {
        this.internalCode = internalCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}