package com.faisal.taskmanager.task;

import com.faisal.taskmanager.utils.Interfaces.BaseResponseDtoInterface;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@Schema(
        name = "TaskResponse",
        description = "Schema to hold Task information as a response"
)
public class TaskResponseDto implements BaseResponseDtoInterface {

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
