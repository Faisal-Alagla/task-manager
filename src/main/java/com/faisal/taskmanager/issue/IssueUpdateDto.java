package com.faisal.taskmanager.issue;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        name = "IssueUpdateRequest",
        description = "Schema to hold Issue information"
)
public class IssueUpdateDto {

    @NotBlank(message = "issue name can't be empty")
    @Size(max = 50, message = "name length can't be greater than {max} characters")
    private String name;

    private String description;

    private Integer statusId;

    private Integer criticalityId;

}
