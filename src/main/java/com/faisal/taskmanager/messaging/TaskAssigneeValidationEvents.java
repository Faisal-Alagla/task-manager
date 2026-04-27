package com.faisal.taskmanager.messaging;

public final class TaskAssigneeValidationEvents {

    public static final String REQUEST_EVENT_TYPE = "task.assignee.validation.request";
    public static final String RESULT_EVENT_TYPE = "task.assignee.validation.result";

    private TaskAssigneeValidationEvents() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
