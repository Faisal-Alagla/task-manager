package com.faisal.taskmanager.task;

import com.faisal.taskmanager.common.exceptions.ErrorMessage;
import com.faisal.taskmanager.common.lookups.LookupType;
import com.faisal.taskmanager.utils.validations.LookupValidation;
import com.faisal.taskmanager.utils.validations.TaskExistsValidation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
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
    @TaskExistsValidation(nullable = true)
    private UUID parentTaskId;

    @NotBlank(message = "task name can't be empty")
    @Size(max = 50, message = "name length can't be greater than 50 characters")
    private String name;

    private UUID assigneeId;

    private LocalDateTime dueDate;

    private String description;

    @LookupValidation(
            lookupType = LookupType.TASK_STATUS,
            errorMessage = ErrorMessage.TASK_STATUS_NOT_FOUND
    )
    private Integer statusId;

    @LookupValidation(
            lookupType = LookupType.TASK_PRIORITY,
            errorMessage = ErrorMessage.TASK_PRIORITY_NOT_FOUND
    )
    private Integer priorityId;

}
