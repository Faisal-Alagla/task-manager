package com.faisal.taskmanager.issue;

import com.faisal.taskmanager.utils.validations.TaskExistsValidation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(
        name = "IssueCreationRequest",
        description = "Schema to hold Issue information"
)
public class IssueCreationDto {

    @NotBlank(message = "issue name can't be empty")
    @Size(max = 50, message = "name length can't be greater than {max} characters")
    private String name;

    private String description;

    private Integer statusId;

    private Integer criticalityId;

    @Schema(
            description = "The Id of the associated task",
            example = "dd4a292c-13fc-449b-aa0f-00582f3db380"
    )
    @TaskExistsValidation
    private UUID taskId;

}
