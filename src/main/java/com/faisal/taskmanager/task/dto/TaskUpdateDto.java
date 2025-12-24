package com.faisal.taskmanager.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Schema(
        name = "TaskUpdateRequest",
        description = "Schema to hold Task information"
)
public class TaskUpdateDto {

    @NotBlank(message = "task name can't be empty")
    @Size(max = 50, message = "name length can't be greater than {max} characters")
    private String name;

    private UUID assigneeId;

    private Instant dueDate;

    private String description;

    private Integer statusId;

    private Integer priorityId;

}
