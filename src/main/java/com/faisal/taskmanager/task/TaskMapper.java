package com.faisal.taskmanager.task;


import jakarta.persistence.Tuple;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TaskMapper {

    /**
     * maps a TaskCreationDto object to Task
     *
     * @param taskCreationDto The TaskCreationDto object
     * @return Task object containing the passed TaskCreationDto data
     */
    public static Task mapToTask(TaskCreationDto taskCreationDto) {
        return Task.builder()
                .name(taskCreationDto.getName())
                .assigneeId(taskCreationDto.getAssigneeId())
                .dueDate(taskCreationDto.getDueDate())
                .description(taskCreationDto.getDescription())
                .statusId(taskCreationDto.getStatusId())
                .priorityId(taskCreationDto.getPriorityId())
                .build();
    }

    /**
     * maps a Tuple object to TaskResponseDto
     *
     * @param tuple The database result tuple when fetching a task with the ids of its relations
     * @return TaskResponseDto object containing the fetched data
     */
    public static TaskResponseDto mapToTaskResponseFromTuple(Tuple tuple) {
        if (tuple == null) {
            return null;
        }

        Timestamp dueDateTimestamp = tuple.get("dueDate", Timestamp.class);
        LocalDateTime dueDate = dueDateTimestamp != null ? dueDateTimestamp.toLocalDateTime() : null;

        UUID[] issuesArray = tuple.get("issuesIds", UUID[].class);
        List<UUID> issuesIds = issuesArray != null ? Arrays.asList(issuesArray) : new ArrayList<>();

        UUID[] childTasksArray = tuple.get("childTaskIds", UUID[].class);
        List<UUID> childTaskIds = childTasksArray != null ? Arrays.asList(childTasksArray) : new ArrayList<>();

        return new TaskResponseDto(
                tuple.get("id", UUID.class),
                tuple.get("name", String.class),
                tuple.get("assigneeId", UUID.class),
                dueDate,
                tuple.get("description", String.class),
                tuple.get("statusId", Integer.class),
                tuple.get("priorityId", Integer.class),
                issuesIds,
                tuple.get("parentTaskId", UUID.class),
                childTaskIds
        );
    }

}
