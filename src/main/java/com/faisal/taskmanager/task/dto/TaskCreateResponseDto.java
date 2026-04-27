package com.faisal.taskmanager.task.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(
        name = "TaskCreateResponse",
        description = "Schema holding the created task and an optional warning"
)
public class TaskCreateResponseDto {

    private TaskResponseDto task;

    private String warning;

}
