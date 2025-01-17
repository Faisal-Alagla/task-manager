package com.faisal.taskmanager.issue;

import com.faisal.taskmanager.common.exceptions.ErrorMessage;
import com.faisal.taskmanager.common.lookups.LookupType;
import com.faisal.taskmanager.utils.validations.LookupValidation;
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
    @Size(max = 50, message = "name length can't be greater than 50 characters")
    private String name;

    private String description;

    @LookupValidation(
            lookupType = LookupType.ISSUE_STATUS,
            errorMessage = ErrorMessage.ISSUE_STATUS_NOT_FOUND
    )
    private Integer statusId;

    @LookupValidation(
            lookupType = LookupType.ISSUE_CRITICALITY,
            errorMessage = ErrorMessage.ISSUE_CRITICALITY_NOT_FOUND
    )
    private Integer criticalityId;

}
