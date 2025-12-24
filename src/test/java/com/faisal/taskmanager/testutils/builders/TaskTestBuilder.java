package com.faisal.taskmanager.testutils.builders;

import com.faisal.taskmanager.task.Task;

import java.time.Instant;
import java.util.UUID;

import static com.faisal.taskmanager.testutils.constants.TestConstants.*;

public class TaskTestBuilder {

    private UUID id = TASK_ID_1;
    private String name = TASK_NAME;
    private String description = TASK_DESCRIPTION;
    private UUID assigneeId = null;
    private Instant dueDate = null;
    private Integer statusId = TASK_STATUS_IN_PROGRESS;
    private Integer priorityId = TASK_PRIORITY_MEDIUM;

    public static TaskTestBuilder aTask() {
        return new TaskTestBuilder();
    }

    public TaskTestBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public TaskTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TaskTestBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public TaskTestBuilder withAssigneeId(UUID assigneeId) {
        this.assigneeId = assigneeId;
        return this;
    }

    public TaskTestBuilder withDueDate(Instant dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public TaskTestBuilder withStatusId(Integer statusId) {
        this.statusId = statusId;
        return this;
    }

    public TaskTestBuilder withPriorityId(Integer priorityId) {
        this.priorityId = priorityId;
        return this;
    }

    public TaskTestBuilder asCompleted() {
        this.statusId = TASK_STATUS_COMPLETED;
        return this;
    }

    public Task build() {
        Task task = new Task();
        task.setId(id);
        task.setName(name);
        task.setDescription(description);
        task.setAssigneeId(assigneeId);
        task.setDueDate(dueDate);
        task.setStatusId(statusId);
        task.setPriorityId(priorityId);
        return task;
    }
}
