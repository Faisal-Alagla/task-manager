package com.faisal.taskmanager.issue;

import com.faisal.taskmanager.common.exceptions.ErrorDetailBuilder;
import com.faisal.taskmanager.common.exceptions.ErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Validator for Issue-related business rules and constraints.
 *
 * <p>Handles validation logic for issue operations. Separated from the service layer for better
 * testability and maintainability.</p>
 *
 * @author Faisal
 */
@Component
@RequiredArgsConstructor
public class IssueValidator {

    // Field name constants
    private static final String FIELD_CRITICALITY_ID = "criticalityId";
    private static final String FIELD_STATUS_ID = "statusId";

    @Setter
    private IssueLookupContext issueLookupContext;

    /**
     * Validates all lookups for issue creation
     *
     * @param criticalityId the criticality ID to validate
     * @param statusId the status ID to validate
     * @throws com.faisal.taskmanager.common.exceptions.HandledException if validation fails
     */
    public void validateIssueCreationLookups(Integer criticalityId, Integer statusId) {
        new ErrorDetailBuilder()
                .addIf(!issueLookupContext.getIssueCriticalityIds().contains(criticalityId),
                        ErrorMessage.ISSUE_CRITICALITY_NOT_FOUND,
                        FIELD_CRITICALITY_ID)
                .addIf(!issueLookupContext.getIssueStatusIds().contains(statusId),
                        ErrorMessage.ISSUE_STATUS_NOT_FOUND,
                        FIELD_STATUS_ID)
                .throwIfHasErrors();
    }

    /**
     * Validates lookups for issue update (all fields optional)
     *
     * @param criticalityId the criticality ID to validate (nullable)
     * @param statusId the status ID to validate (nullable)
     * @throws com.faisal.taskmanager.common.exceptions.HandledException if validation fails
     */
    public void validateIssueUpdateLookups(Integer criticalityId, Integer statusId) {
        ErrorDetailBuilder builder = new ErrorDetailBuilder();

        if (criticalityId != null) {
            builder.addIf(!issueLookupContext.getIssueCriticalityIds().contains(criticalityId),
                    ErrorMessage.ISSUE_CRITICALITY_NOT_FOUND,
                    FIELD_CRITICALITY_ID);
        }

        if (statusId != null) {
            builder.addIf(!issueLookupContext.getIssueStatusIds().contains(statusId),
                    ErrorMessage.ISSUE_STATUS_NOT_FOUND,
                    FIELD_STATUS_ID);
        }

        builder.throwIfHasErrors();
    }

}
