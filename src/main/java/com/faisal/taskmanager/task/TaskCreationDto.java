package com.faisal.taskmanager.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Schema(
        name = "Task",
        description = "Schema to hold Task information"
)
public class TaskCreationDto {

    @NotBlank(message = "task name can't be empty")
    @Size(max = 50, message = "name length can't be greater than 50 characters")
    private String name;

    private UUID assigneeId;

    private LocalDateTime dueDate;

    private String description;

    private Integer statusId;

    private Integer priorityId;

}
