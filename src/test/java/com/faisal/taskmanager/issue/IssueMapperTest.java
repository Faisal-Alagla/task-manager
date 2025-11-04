package com.faisal.taskmanager.issue;

import com.faisal.taskmanager.issue.dto.IssueCreationDto;
import com.faisal.taskmanager.issue.dto.IssueResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.faisal.taskmanager.testutils.builders.IssueCreationDtoBuilder.anIssueCreationDto;
import static com.faisal.taskmanager.testutils.builders.IssueTestBuilder.anIssue;
import static com.faisal.taskmanager.testutils.constants.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("IssueMapper Tests")
class IssueMapperTest {

    @Test
    @DisplayName("Should map IssueCreationDto to Issue entity correctly")
    void mapToIssue_ShouldMapAllFields() {
        // Arrange
        IssueCreationDto dto = anIssueCreationDto()
                .withName(ISSUE_NAME)
                .withDescription(ISSUE_DESCRIPTION)
                .withCriticalityId(ISSUE_CRITICALITY_HIGH)
                .withStatusId(ISSUE_STATUS_IN_PROGRESS)
                .withTaskId(TASK_ID_1)
                .build();

        // Act
        Issue result = IssueMapper.mapToIssue(dto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(ISSUE_NAME);
        assertThat(result.getDescription()).isEqualTo(ISSUE_DESCRIPTION);
        assertThat(result.getCriticalityId()).isEqualTo(ISSUE_CRITICALITY_HIGH);
        assertThat(result.getStatusId()).isEqualTo(ISSUE_STATUS_IN_PROGRESS);
        assertThat(result.getTaskId()).isEqualTo(TASK_ID_1);
    }

    @Test
    @DisplayName("Should handle null fields in IssueCreationDto")
    void mapToIssue_ShouldHandleNullFields() {
        // Arrange
        IssueCreationDto dto = anIssueCreationDto()
                .withName(ISSUE_NAME)
                .withDescription(null)
                .build();

        // Act
        Issue result = IssueMapper.mapToIssue(dto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(ISSUE_NAME);
        assertThat(result.getDescription()).isNull();
    }

    @Test
    @DisplayName("Should map Issue entity to IssueResponseDto correctly")
    void mapToIssueResponseDto_ShouldMapAllFields() {
        // Arrange
        Issue issue = anIssue()
                .withId(ISSUE_ID_1)
                .withName(ISSUE_NAME)
                .withDescription(ISSUE_DESCRIPTION)
                .withCriticalityId(ISSUE_CRITICALITY_HIGH)
                .withStatusId(ISSUE_STATUS_RESOLVED)
                .withTaskId(TASK_ID_1)
                .build();

        // Act
        IssueResponseDto result = IssueMapper.mapToIssueResponseDto(issue);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(ISSUE_ID_1);
        assertThat(result.getName()).isEqualTo(ISSUE_NAME);
        assertThat(result.getDescription()).isEqualTo(ISSUE_DESCRIPTION);
        assertThat(result.getCriticalityId()).isEqualTo(ISSUE_CRITICALITY_HIGH);
        assertThat(result.getStatusId()).isEqualTo(ISSUE_STATUS_RESOLVED);
        assertThat(result.getTaskId()).isEqualTo(TASK_ID_1);
    }

    @Test
    @DisplayName("Should handle null description in Issue entity")
    void mapToIssueResponseDto_ShouldHandleNullDescription() {
        // Arrange
        Issue issue = anIssue()
                .withId(ISSUE_ID_1)
                .withName(ISSUE_NAME)
                .withDescription(null)
                .build();

        // Act
        IssueResponseDto result = IssueMapper.mapToIssueResponseDto(issue);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isNull();
    }
}
