package com.faisal.taskmanager.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Schema(
        name = "TaskResponse",
        description = "Schema to hold Task information as a response"
)
public class TaskResponseDto {

    private UUID id;

    private String name;

    private UUID assigneeId;

    private LocalDateTime dueDate;

    private String description;

    private Integer statusId;

    private Integer priorityId;

    private List<UUID> issuesIds;

    private UUID parentTaskId;

    private List<UUID> childTaskIds;

}
