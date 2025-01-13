package com.faisal.taskmanager.common.exceptions;

import lombok.Getter;

@Getter
public enum ErrorMessage {

    //validation
    CONSTRAINT_VIOLATED_EXCEPTION(1_000, "Constraint violated exception"),
    INVALID_REQUEST_PAYLOAD(1_001, "The request content is not valid and could not be deserialized."),
    INVALID_REQUEST_ATTRIBUTES(1_002, "Invalid request attributes"),
    INVALID_PATH(1_003, "Invalid path value"),
    MISSING_REQUEST_PARAMETER(1_004, "Required parameter is missing"),
    UNSUPPORTED_REQUEST_METHOD(1_005, "Request method is not supported"),
    INVALID_API_USAGE(1_007, "Invalid API usage"),
    DATA_INTEGRITY_VIOLATION_EXCEPTION(1_007, "Data integrity violation exception"),

    //task
    TASK_NOT_FOUND(2_000, "Task not found"),

    //issue
    ISSUE_NOT_FOUND(2_100, "Issue not found"),

    //lookup,
    ISSUE_CRITICALITY_NOT_FOUND(3_000, "Issue criticality not found"),
    ISSUE_STATUS_NOT_FOUND(3_001, "Issue status not found"),
    TASK_PRIORITY_NOT_FOUND(3_001, "Task priority not found"),
    TASK_STATUS_NOT_FOUND(3_001, "Task status not found"),

    //activity

    //general
    INTERNAL_SERVER_ERROR(5_000, "Internal Server Error");

    private final Integer internalCode;
    private final String message;

    ErrorMessage(Integer internalCode, String message) {
        this.internalCode = internalCode;
        this.message = message;
    }
}
