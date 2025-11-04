package com.faisal.taskmanager.testutils.fixtures;

import com.faisal.taskmanager.common.lookups.domain.IssueCriticalityLookupCollection;
import com.faisal.taskmanager.common.lookups.domain.IssueStatusLookupCollection;
import com.faisal.taskmanager.common.lookups.domain.TaskPriorityLookupCollection;
import com.faisal.taskmanager.common.lookups.domain.TaskStatusLookupCollection;
import com.faisal.taskmanager.common.lookups.entities.IssueCriticalityLk;
import com.faisal.taskmanager.common.lookups.entities.IssueStatusLk;
import com.faisal.taskmanager.common.lookups.entities.TaskPriorityLk;
import com.faisal.taskmanager.common.lookups.entities.TaskStatusLk;

import java.util.List;

import static com.faisal.taskmanager.testutils.constants.TestConstants.*;

/**
 * Factory for creating mock lookup entities and collections for testing.
 * Provides pre-configured lookup data matching the seed data in the database.
 */
public final class MockLookupFactory {

    private MockLookupFactory() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // ==================== Task Status ====================

    public static TaskStatusLk createTaskStatusInProgress() {
        return TaskStatusLk.builder()
                .id(TASK_STATUS_IN_PROGRESS)
                .name(TASK_STATUS_IN_PROGRESS_NAME)
                .build();
    }

    public static TaskStatusLk createTaskStatusCompleted() {
        return TaskStatusLk.builder()
                .id(TASK_STATUS_COMPLETED)
                .name(TASK_STATUS_COMPLETED_NAME)
                .build();
    }

    public static TaskStatusLk createTaskStatusCancelled() {
        return TaskStatusLk.builder()
                .id(TASK_STATUS_CANCELLED)
                .name(TASK_STATUS_CANCELLED_NAME)
                .build();
    }

    public static TaskStatusLk createTaskStatusOnHold() {
        return TaskStatusLk.builder()
                .id(TASK_STATUS_ON_HOLD)
                .name(TASK_STATUS_ON_HOLD_NAME)
                .build();
    }

    public static List<TaskStatusLk> createAllTaskStatuses() {
        return List.of(
                createTaskStatusInProgress(),
                createTaskStatusCompleted(),
                createTaskStatusCancelled(),
                createTaskStatusOnHold()
        );
    }

    public static TaskStatusLookupCollection createTaskStatusCollection() {
        return new TaskStatusLookupCollection(createAllTaskStatuses());
    }

    // ==================== Task Priority ====================

    public static TaskPriorityLk createTaskPriorityHigh() {
        return TaskPriorityLk.builder()
                .id(TASK_PRIORITY_HIGH)
                .name(TASK_PRIORITY_HIGH_NAME)
                .build();
    }

    public static TaskPriorityLk createTaskPriorityMedium() {
        return TaskPriorityLk.builder()
                .id(TASK_PRIORITY_MEDIUM)
                .name(TASK_PRIORITY_MEDIUM_NAME)
                .build();
    }

    public static TaskPriorityLk createTaskPriorityLow() {
        return TaskPriorityLk.builder()
                .id(TASK_PRIORITY_LOW)
                .name(TASK_PRIORITY_LOW_NAME)
                .build();
    }

    public static List<TaskPriorityLk> createAllTaskPriorities() {
        return List.of(
                createTaskPriorityHigh(),
                createTaskPriorityMedium(),
                createTaskPriorityLow()
        );
    }

    public static TaskPriorityLookupCollection createTaskPriorityCollection() {
        return new TaskPriorityLookupCollection(createAllTaskPriorities());
    }

    // ==================== Issue Status ====================

    public static IssueStatusLk createIssueStatusInProgress() {
        return IssueStatusLk.builder()
                .id(ISSUE_STATUS_IN_PROGRESS)
                .name(ISSUE_STATUS_IN_PROGRESS_NAME)
                .build();
    }

    public static IssueStatusLk createIssueStatusResolved() {
        return IssueStatusLk.builder()
                .id(ISSUE_STATUS_RESOLVED)
                .name(ISSUE_STATUS_RESOLVED_NAME)
                .build();
    }

    public static List<IssueStatusLk> createAllIssueStatuses() {
        return List.of(
                createIssueStatusInProgress(),
                createIssueStatusResolved()
        );
    }

    public static IssueStatusLookupCollection createIssueStatusCollection() {
        return new IssueStatusLookupCollection(createAllIssueStatuses());
    }

    // ==================== Issue Criticality ====================

    public static IssueCriticalityLk createIssueCriticalityHigh() {
        return IssueCriticalityLk.builder()
                .id(ISSUE_CRITICALITY_HIGH)
                .name(ISSUE_CRITICALITY_HIGH_NAME)
                .build();
    }

    public static IssueCriticalityLk createIssueCriticalityMedium() {
        return IssueCriticalityLk.builder()
                .id(ISSUE_CRITICALITY_MEDIUM)
                .name(ISSUE_CRITICALITY_MEDIUM_NAME)
                .build();
    }

    public static IssueCriticalityLk createIssueCriticalityLow() {
        return IssueCriticalityLk.builder()
                .id(ISSUE_CRITICALITY_LOW)
                .name(ISSUE_CRITICALITY_LOW_NAME)
                .build();
    }

    public static List<IssueCriticalityLk> createAllIssueCriticalities() {
        return List.of(
                createIssueCriticalityHigh(),
                createIssueCriticalityMedium(),
                createIssueCriticalityLow()
        );
    }

    public static IssueCriticalityLookupCollection createIssueCriticalityCollection() {
        return new IssueCriticalityLookupCollection(createAllIssueCriticalities());
    }
}
