package com.faisal.taskmanager.testutils.builders;

import com.faisal.taskmanager.task.dto.TaskCreationDto;

import java.time.Instant;
import java.util.UUID;

import static com.faisal.taskmanager.testutils.constants.TestConstants.*;

public class TaskCreationDtoBuilder {

    private UUID parentTaskId = null;
    private String name = TASK_NAME;
    private UUID assigneeId = null;
    private Instant dueDate = null;
    private String description = TASK_DESCRIPTION;
    private Integer statusId = TASK_STATUS_IN_PROGRESS; // Default: in_progress
    private Integer priorityId = TASK_PRIORITY_MEDIUM; // Default: medium

    public static TaskCreationDtoBuilder aTaskCreationDto() {
        return new TaskCreationDtoBuilder();
    }

    public TaskCreationDtoBuilder withParentTaskId(UUID parentTaskId) {
        this.parentTaskId = parentTaskId;
        return this;
    }

    public TaskCreationDtoBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TaskCreationDtoBuilder withAssigneeId(UUID assigneeId) {
        this.assigneeId = assigneeId;
        return this;
    }

    public TaskCreationDtoBuilder withDueDate(Instant dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public TaskCreationDtoBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public TaskCreationDtoBuilder withStatusId(Integer statusId) {
        this.statusId = statusId;
        return this;
    }

    public TaskCreationDtoBuilder withPriorityId(Integer priorityId) {
        this.priorityId = priorityId;
        return this;
    }

    public TaskCreationDtoBuilder asCompleted() {
        this.statusId = TASK_STATUS_COMPLETED;
        return this;
    }

    public TaskCreationDto build() {
        TaskCreationDto dto = new TaskCreationDto();
        dto.setParentTaskId(parentTaskId);
        dto.setName(name);
        dto.setAssigneeId(assigneeId);
        dto.setDueDate(dueDate);
        dto.setDescription(description);
        dto.setStatusId(statusId);
        dto.setPriorityId(priorityId);
        return dto;
    }
}
