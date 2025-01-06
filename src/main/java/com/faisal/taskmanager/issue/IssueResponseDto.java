package com.faisal.taskmanager.issue;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@Schema(
        name = "IssueResponse",
        description = "Schema to hold Issue information as a response"
)
public class IssueResponseDto {

    private UUID id;

    private String name;

    private String description;

    private Integer statusId;

    private Integer criticalityId;

    private UUID taskId;

}
