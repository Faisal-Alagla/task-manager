package com.faisal.taskmanager.testutils.builders;

import com.faisal.taskmanager.issue.dto.IssueUpdateDto;

import static com.faisal.taskmanager.testutils.constants.TestConstants.*;

public class IssueUpdateDtoBuilder {

    private String name = ISSUE_NAME_UPDATED;
    private String description = ISSUE_DESCRIPTION_UPDATED;
    private Integer criticalityId = ISSUE_CRITICALITY_HIGH; // Default: high
    private Integer statusId = ISSUE_STATUS_RESOLVED; // Default: resolved

    public static IssueUpdateDtoBuilder anIssueUpdateDto() {
        return new IssueUpdateDtoBuilder();
    }

    public IssueUpdateDtoBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public IssueUpdateDtoBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public IssueUpdateDtoBuilder withCriticalityId(Integer criticalityId) {
        this.criticalityId = criticalityId;
        return this;
    }

    public IssueUpdateDtoBuilder withStatusId(Integer statusId) {
        this.statusId = statusId;
        return this;
    }

    public IssueUpdateDto build() {
        IssueUpdateDto dto = new IssueUpdateDto();
        dto.setName(name);
        dto.setDescription(description);
        dto.setCriticalityId(criticalityId);
        dto.setStatusId(statusId);
        return dto;
    }
}
