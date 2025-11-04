package com.faisal.taskmanager.testutils.builders;

import com.faisal.taskmanager.issue.dto.IssueCreationDto;

import java.util.UUID;

import static com.faisal.taskmanager.testutils.constants.TestConstants.*;

public class IssueCreationDtoBuilder {

    private String name = ISSUE_NAME;
    private String description = ISSUE_DESCRIPTION;
    private Integer criticalityId = ISSUE_CRITICALITY_MEDIUM; // Default: medium
    private Integer statusId = ISSUE_STATUS_IN_PROGRESS; // Default: in_progress
    private UUID taskId = TASK_ID_1;

    public static IssueCreationDtoBuilder anIssueCreationDto() {
        return new IssueCreationDtoBuilder();
    }

    public IssueCreationDtoBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public IssueCreationDtoBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public IssueCreationDtoBuilder withCriticalityId(Integer criticalityId) {
        this.criticalityId = criticalityId;
        return this;
    }

    public IssueCreationDtoBuilder withStatusId(Integer statusId) {
        this.statusId = statusId;
        return this;
    }

    public IssueCreationDtoBuilder withTaskId(UUID taskId) {
        this.taskId = taskId;
        return this;
    }

    public IssueCreationDto build() {
        IssueCreationDto dto = new IssueCreationDto();
        dto.setName(name);
        dto.setDescription(description);
        dto.setCriticalityId(criticalityId);
        dto.setStatusId(statusId);
        dto.setTaskId(taskId);
        return dto;
    }
}
