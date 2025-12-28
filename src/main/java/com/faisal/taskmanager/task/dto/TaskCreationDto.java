package com.faisal.taskmanager.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Schema(
        name = "TaskCreationRequest",
        description = "Schema to hold Task information"
)
public class TaskCreationDto {

    @Schema(
            description = "ID of the parent task",
            example = "dd4a292c-13fc-449b-aa0f-00582f3db380",
            nullable = true
    )
    private UUID parentTaskId;

    @NotBlank(message = "task name can't be empty")
    @Size(max = 50, message = "name length can't be greater than {max} characters")
    private String name;

    private UUID assigneeId;

    private Instant dueDate;

    private String description;

    private Integer statusId;

    private Integer priorityId;

}
