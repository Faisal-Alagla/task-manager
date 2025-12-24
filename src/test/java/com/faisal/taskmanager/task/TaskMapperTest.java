package com.faisal.taskmanager.task;

import com.faisal.taskmanager.task.dto.TaskCreationDto;
import com.faisal.taskmanager.task.dto.TaskResponseDto;
import jakarta.persistence.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static com.faisal.taskmanager.testutils.builders.TaskCreationDtoBuilder.aTaskCreationDto;
import static com.faisal.taskmanager.testutils.constants.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("TaskMapper Tests")
class TaskMapperTest {

    @Test
    @DisplayName("Should map TaskCreationDto to Task entity correctly")
    void mapToTask_ShouldMapAllFields() {
        // Arrange
        TaskCreationDto dto = aTaskCreationDto()
                .withName(TASK_NAME)
                .withDescription(TASK_DESCRIPTION)
                .withStatusId(TASK_STATUS_IN_PROGRESS)
                .withPriorityId(TASK_PRIORITY_MEDIUM)
                .withAssigneeId(USER_ID_1)
                .withDueDate(BASE_TIMESTAMP)
                .build();

        // Act
        Task result = TaskMapper.mapToTask(dto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(TASK_NAME);
        assertThat(result.getDescription()).isEqualTo(TASK_DESCRIPTION);
        assertThat(result.getStatusId()).isEqualTo(TASK_STATUS_IN_PROGRESS);
        assertThat(result.getPriorityId()).isEqualTo(TASK_PRIORITY_MEDIUM);
        assertThat(result.getAssigneeId()).isEqualTo(USER_ID_1);
        assertThat(result.getDueDate()).isEqualTo(BASE_TIMESTAMP);
    }

    @Test
    @DisplayName("Should handle null fields in TaskCreationDto")
    void mapToTask_ShouldHandleNullFields() {
        // Arrange
        TaskCreationDto dto = aTaskCreationDto()
                .withName(TASK_NAME)
                .withDescription(null)
                .withAssigneeId(null)
                .withDueDate(null)
                .build();

        // Act
        Task result = TaskMapper.mapToTask(dto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(TASK_NAME);
        assertThat(result.getDescription()).isNull();
        assertThat(result.getAssigneeId()).isNull();
        assertThat(result.getDueDate()).isNull();
    }

    @Test
    @DisplayName("Should map Tuple to TaskResponseDto correctly")
    void mapToTaskResponseFromTuple_ShouldMapAllFields() {
        // Arrange
        Tuple tuple = mock(Tuple.class);
        UUID taskId = TASK_ID_1;
        UUID[] issueIds = {ISSUE_ID_1, ISSUE_ID_2};
        UUID[] childTaskIds = {TASK_ID_2, TASK_ID_3};

        when(tuple.get("id", UUID.class)).thenReturn(taskId);
        when(tuple.get("name", String.class)).thenReturn(TASK_NAME);
        when(tuple.get("assigneeId", UUID.class)).thenReturn(USER_ID_1);
        when(tuple.get("dueDate", Instant.class)).thenReturn(BASE_TIMESTAMP);
        when(tuple.get("description", String.class)).thenReturn(TASK_DESCRIPTION);
        when(tuple.get("statusId", Integer.class)).thenReturn(TASK_STATUS_IN_PROGRESS);
        when(tuple.get("priorityId", Integer.class)).thenReturn(TASK_PRIORITY_MEDIUM);
        when(tuple.get("issuesIds", UUID[].class)).thenReturn(issueIds);
        when(tuple.get("parentTaskId", UUID.class)).thenReturn(PARENT_TASK_ID);
        when(tuple.get("childTaskIds", UUID[].class)).thenReturn(childTaskIds);

        // Act
        TaskResponseDto result = TaskMapper.mapToTaskResponseFromTuple(tuple);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(taskId);
        assertThat(result.getName()).isEqualTo(TASK_NAME);
        assertThat(result.getAssigneeId()).isEqualTo(USER_ID_1);
        assertThat(result.getDueDate()).isEqualTo(BASE_TIMESTAMP);
        assertThat(result.getDescription()).isEqualTo(TASK_DESCRIPTION);
        assertThat(result.getStatusId()).isEqualTo(TASK_STATUS_IN_PROGRESS);
        assertThat(result.getPriorityId()).isEqualTo(TASK_PRIORITY_MEDIUM);
        assertThat(result.getIssuesIds()).containsExactly(ISSUE_ID_1, ISSUE_ID_2);
        assertThat(result.getParentTaskId()).isEqualTo(PARENT_TASK_ID);
        assertThat(result.getChildTaskIds()).containsExactly(TASK_ID_2, TASK_ID_3);
    }

    @Test
    @DisplayName("Should handle null Tuple")
    void mapToTaskResponseFromTuple_WithNullTuple_ShouldReturnNull() {
        // Act
        TaskResponseDto result = TaskMapper.mapToTaskResponseFromTuple(null);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should handle null arrays in Tuple")
    void mapToTaskResponseFromTuple_WithNullArrays_ShouldReturnEmptyLists() {
        // Arrange
        Tuple tuple = mock(Tuple.class);
        when(tuple.get("id", UUID.class)).thenReturn(TASK_ID_1);
        when(tuple.get("name", String.class)).thenReturn(TASK_NAME);
        when(tuple.get("assigneeId", UUID.class)).thenReturn(null);
        when(tuple.get("dueDate", Instant.class)).thenReturn(null);
        when(tuple.get("description", String.class)).thenReturn(TASK_DESCRIPTION);
        when(tuple.get("statusId", Integer.class)).thenReturn(TASK_STATUS_IN_PROGRESS);
        when(tuple.get("priorityId", Integer.class)).thenReturn(TASK_PRIORITY_MEDIUM);
        when(tuple.get("issuesIds", UUID[].class)).thenReturn(null);
        when(tuple.get("parentTaskId", UUID.class)).thenReturn(null);
        when(tuple.get("childTaskIds", UUID[].class)).thenReturn(null);

        // Act
        TaskResponseDto result = TaskMapper.mapToTaskResponseFromTuple(tuple);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getDueDate()).isNull();
        assertThat(result.getIssuesIds()).isEmpty();
        assertThat(result.getParentTaskId()).isNull();
        assertThat(result.getChildTaskIds()).isEmpty();
    }
}
