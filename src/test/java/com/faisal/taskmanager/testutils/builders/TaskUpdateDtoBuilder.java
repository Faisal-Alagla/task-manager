package com.faisal.taskmanager.testutils.builders;

import com.faisal.taskmanager.task.dto.TaskUpdateDto;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.faisal.taskmanager.testutils.constants.TestConstants.*;

public class TaskUpdateDtoBuilder {

    private String name = TASK_NAME_UPDATED;
    private UUID assigneeId = null;
    private LocalDateTime dueDate = null;
    private String description = TASK_DESCRIPTION_UPDATED;
    private Integer statusId = TASK_STATUS_IN_PROGRESS;
    private Integer priorityId = TASK_PRIORITY_HIGH;

    public static TaskUpdateDtoBuilder aTaskUpdateDto() {
        return new TaskUpdateDtoBuilder();
    }

    public TaskUpdateDtoBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TaskUpdateDtoBuilder withAssigneeId(UUID assigneeId) {
        this.assigneeId = assigneeId;
        return this;
    }

    public TaskUpdateDtoBuilder withDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public TaskUpdateDtoBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public TaskUpdateDtoBuilder withStatusId(Integer statusId) {
        this.statusId = statusId;
        return this;
    }

    public TaskUpdateDtoBuilder withPriorityId(Integer priorityId) {
        this.priorityId = priorityId;
        return this;
    }

    public TaskUpdateDtoBuilder asCompleted() {
        this.statusId = TASK_STATUS_COMPLETED;
        return this;
    }

    public TaskUpdateDto build() {
        TaskUpdateDto dto = new TaskUpdateDto();
        dto.setName(name);
        dto.setAssigneeId(assigneeId);
        dto.setDueDate(dueDate);
        dto.setDescription(description);
        dto.setStatusId(statusId);
        dto.setPriorityId(priorityId);
        return dto;
    }
}
