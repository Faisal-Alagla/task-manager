package com.faisal.taskmanager.testutils.constants;

import com.faisal.taskmanager.common.lookups.enums.IssueCriticalityEnum;
import com.faisal.taskmanager.common.lookups.enums.IssueStatusEnum;
import com.faisal.taskmanager.common.lookups.enums.TaskPriorityEnum;
import com.faisal.taskmanager.common.lookups.enums.TaskStatusEnum;

import java.time.LocalDateTime;
import java.util.UUID;

public final class TestConstants {

    private TestConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // ==================== UUIDs ====================

    // Task UUIDs
    public static final UUID TASK_ID_1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
    public static final UUID TASK_ID_2 = UUID.fromString("22222222-2222-2222-2222-222222222222");
    public static final UUID TASK_ID_3 = UUID.fromString("33333333-3333-3333-3333-333333333333");
    public static final UUID PARENT_TASK_ID = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    public static final UUID NON_EXISTENT_TASK_ID = UUID.fromString("99999999-9999-9999-9999-999999999999");

    // Issue UUIDs
    public static final UUID ISSUE_ID_1 = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
    public static final UUID ISSUE_ID_2 = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc");
    public static final UUID NON_EXISTENT_ISSUE_ID = UUID.fromString("88888888-8888-8888-8888-888888888888");

    // User UUIDs
    public static final UUID USER_ID_1 = UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd");
    public static final UUID USER_ID_2 = UUID.fromString("eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee");

    // ==================== LOOKUP IDs (from enums) ====================

    // Task Status - Reference enum directly
    public static final Integer TASK_STATUS_IN_PROGRESS = TaskStatusEnum.IN_PROGRESS.getId();
    public static final Integer TASK_STATUS_COMPLETED = TaskStatusEnum.COMPLETED.getId();
    public static final Integer TASK_STATUS_CANCELLED = TaskStatusEnum.CANCELLED.getId();
    public static final Integer TASK_STATUS_ON_HOLD = TaskStatusEnum.ON_HOLD.getId();
    public static final Integer INVALID_TASK_STATUS_ID = 9999;

    public static final String TASK_STATUS_IN_PROGRESS_NAME = TaskStatusEnum.IN_PROGRESS.getName();
    public static final String TASK_STATUS_COMPLETED_NAME = TaskStatusEnum.COMPLETED.getName();
    public static final String TASK_STATUS_CANCELLED_NAME = TaskStatusEnum.CANCELLED.getName();
    public static final String TASK_STATUS_ON_HOLD_NAME = TaskStatusEnum.ON_HOLD.getName();

    // Task Priority - Reference enum directly
    public static final Integer TASK_PRIORITY_HIGH = TaskPriorityEnum.HIGH.getId();
    public static final Integer TASK_PRIORITY_MEDIUM = TaskPriorityEnum.MEDIUM.getId();
    public static final Integer TASK_PRIORITY_LOW = TaskPriorityEnum.LOW.getId();
    public static final Integer INVALID_TASK_PRIORITY_ID = 9999;

    public static final String TASK_PRIORITY_HIGH_NAME = TaskPriorityEnum.HIGH.getName();
    public static final String TASK_PRIORITY_MEDIUM_NAME = TaskPriorityEnum.MEDIUM.getName();
    public static final String TASK_PRIORITY_LOW_NAME = TaskPriorityEnum.LOW.getName();

    // Issue Status - Reference enum directly
    public static final Integer ISSUE_STATUS_IN_PROGRESS = IssueStatusEnum.IN_PROGRESS.getId();
    public static final Integer ISSUE_STATUS_RESOLVED = IssueStatusEnum.RESOLVED.getId();
    public static final Integer INVALID_ISSUE_STATUS_ID = 9999;

    public static final String ISSUE_STATUS_IN_PROGRESS_NAME = IssueStatusEnum.IN_PROGRESS.getName();
    public static final String ISSUE_STATUS_RESOLVED_NAME = IssueStatusEnum.RESOLVED.getName();

    // Issue Criticality - Reference enum directly
    public static final Integer ISSUE_CRITICALITY_HIGH = IssueCriticalityEnum.HIGH.getId();
    public static final Integer ISSUE_CRITICALITY_MEDIUM = IssueCriticalityEnum.MEDIUM.getId();
    public static final Integer ISSUE_CRITICALITY_LOW = IssueCriticalityEnum.LOW.getId();
    public static final Integer INVALID_ISSUE_CRITICALITY_ID = 9999;

    public static final String ISSUE_CRITICALITY_HIGH_NAME = IssueCriticalityEnum.HIGH.getName();
    public static final String ISSUE_CRITICALITY_MEDIUM_NAME = IssueCriticalityEnum.MEDIUM.getName();
    public static final String ISSUE_CRITICALITY_LOW_NAME = IssueCriticalityEnum.LOW.getName();

    // ==================== TEST DATA ====================

    // Sample Task Data
    public static final String TASK_NAME = "Sample Task Name";
    public static final String TASK_DESCRIPTION = "Sample task description with sufficient detail";
    public static final String TASK_NAME_UPDATED = "Updated Task Name";
    public static final String TASK_DESCRIPTION_UPDATED = "Updated task description";

    // Sample Issue Data
    public static final String ISSUE_NAME = "Sample Issue Name";
    public static final String ISSUE_DESCRIPTION = "Sample issue description with detailed information";
    public static final String ISSUE_NAME_UPDATED = "Updated Issue Name";
    public static final String ISSUE_DESCRIPTION_UPDATED = "Updated issue description";

    // Timestamps
    public static final LocalDateTime BASE_TIMESTAMP = LocalDateTime.of(2025, 1, 15, 10, 0, 0);

    // ==================== ACTIVITY LOGGING ====================

    // HTTP Methods
    public static final String HTTP_METHOD_POST = "POST";
    public static final String HTTP_METHOD_PUT = "PUT";
    public static final String HTTP_METHOD_DELETE = "DELETE";
    public static final String HTTP_METHOD_GET = "GET";

    // API Paths
    public static final String API_PATH_TASKS = "/api/v1/tasks";
    public static final String API_PATH_ISSUES = "/api/v1/issues";

    // Operations
    public static final String OPERATION_CREATE = "CREATE";
    public static final String OPERATION_UPDATE = "UPDATE";
    public static final String OPERATION_DELETE = "DELETE";
    public static final String OPERATION_READ = "READ";
}
