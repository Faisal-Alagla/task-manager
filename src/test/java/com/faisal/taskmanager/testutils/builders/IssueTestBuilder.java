package com.faisal.taskmanager.testutils.builders;

import com.faisal.taskmanager.issue.Issue;

import java.util.UUID;

import static com.faisal.taskmanager.testutils.constants.TestConstants.*;

public class IssueTestBuilder {

    private UUID id = ISSUE_ID_1;
    private String name = ISSUE_NAME;
    private String description = ISSUE_DESCRIPTION;
    private Integer criticalityId = ISSUE_CRITICALITY_MEDIUM;
    private Integer statusId = ISSUE_STATUS_IN_PROGRESS;
    private UUID taskId = TASK_ID_1;

    public static IssueTestBuilder anIssue() {
        return new IssueTestBuilder();
    }

    public IssueTestBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public IssueTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public IssueTestBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public IssueTestBuilder withCriticalityId(Integer criticalityId) {
        this.criticalityId = criticalityId;
        return this;
    }

    public IssueTestBuilder withStatusId(Integer statusId) {
        this.statusId = statusId;
        return this;
    }

    public IssueTestBuilder withTaskId(UUID taskId) {
        this.taskId = taskId;
        return this;
    }

    public Issue build() {
        Issue issue = new Issue();
        issue.setId(id);
        issue.setName(name);
        issue.setDescription(description);
        issue.setCriticalityId(criticalityId);
        issue.setStatusId(statusId);
        issue.setTaskId(taskId);
        return issue;
    }
}
