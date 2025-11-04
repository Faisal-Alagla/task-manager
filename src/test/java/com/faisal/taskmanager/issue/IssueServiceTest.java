package com.faisal.taskmanager.issue;

import com.faisal.taskmanager.common.exceptions.ErrorMessage;
import com.faisal.taskmanager.common.exceptions.HandledException;
import com.faisal.taskmanager.common.lookups.LookupService;
import com.faisal.taskmanager.issue.dto.IssueCreationDto;
import com.faisal.taskmanager.issue.dto.IssueResponseDto;
import com.faisal.taskmanager.issue.dto.IssueUpdateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static com.faisal.taskmanager.testutils.builders.IssueCreationDtoBuilder.anIssueCreationDto;
import static com.faisal.taskmanager.testutils.builders.IssueTestBuilder.anIssue;
import static com.faisal.taskmanager.testutils.builders.IssueUpdateDtoBuilder.anIssueUpdateDto;
import static com.faisal.taskmanager.testutils.constants.TestConstants.*;
import static com.faisal.taskmanager.testutils.fixtures.MockLookupFactory.createIssueCriticalityCollection;
import static com.faisal.taskmanager.testutils.fixtures.MockLookupFactory.createIssueStatusCollection;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("IssueService")
class IssueServiceTest {

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private LookupService lookupService;

    @InjectMocks
    private IssueService issueService;

    @BeforeEach
    void setUp() {
        when(lookupService.getIssueStatusCollection()).thenReturn(createIssueStatusCollection());
        when(lookupService.getIssueCriticalityCollection()).thenReturn(createIssueCriticalityCollection());

        issueService = new IssueService(issueRepository, lookupService);
        ReflectionTestUtils.invokeMethod(issueService, "init");
    }

    // ========== createIssue() tests ==========

    @Test
    @DisplayName("createIssue - should validate criticality ID")
    void createIssue_shouldValidateCriticalityId() {
        IssueCreationDto dto = anIssueCreationDto()
                .withCriticalityId(INVALID_ISSUE_CRITICALITY_ID)
                .build();

        assertThatThrownBy(() -> issueService.createIssue(dto))
                .isInstanceOf(HandledException.class)
                .hasFieldOrPropertyWithValue("errorMessage", ErrorMessage.ISSUE_CRITICALITY_NOT_FOUND);
    }

    @Test
    @DisplayName("createIssue - should not call save when criticality invalid")
    void createIssue_shouldNotCallSaveWhenCriticalityInvalid() {
        IssueCreationDto dto = anIssueCreationDto()
                .withCriticalityId(INVALID_ISSUE_CRITICALITY_ID)
                .build();

        try {
            issueService.createIssue(dto);
        } catch (HandledException e) {
            // Expected
        }

        verify(issueRepository, never()).save(any());
    }

    @Test
    @DisplayName("createIssue - should validate status ID")
    void createIssue_shouldValidateStatusId() {
        IssueCreationDto dto = anIssueCreationDto()
                .withStatusId(INVALID_ISSUE_STATUS_ID)
                .build();

        assertThatThrownBy(() -> issueService.createIssue(dto))
                .isInstanceOf(HandledException.class)
                .hasFieldOrPropertyWithValue("errorMessage", ErrorMessage.ISSUE_STATUS_NOT_FOUND);
    }

    @Test
    @DisplayName("createIssue - should not call save when status invalid")
    void createIssue_shouldNotCallSaveWhenStatusInvalid() {
        IssueCreationDto dto = anIssueCreationDto()
                .withStatusId(INVALID_ISSUE_STATUS_ID)
                .build();

        try {
            issueService.createIssue(dto);
        } catch (HandledException e) {
            // Expected
        }

        verify(issueRepository, never()).save(any());
    }

    @Test
    @DisplayName("createIssue - should call save")
    void createIssue_shouldCallSave() {
        IssueCreationDto dto = anIssueCreationDto().build();
        Issue savedIssue = anIssue().build();

        when(issueRepository.save(any(Issue.class))).thenReturn(savedIssue);

        issueService.createIssue(dto);

        verify(issueRepository).save(any(Issue.class));
    }

    @Test
    @DisplayName("createIssue - should return issue response DTO")
    void createIssue_shouldReturnIssueResponseDto() {
        IssueCreationDto dto = anIssueCreationDto().build();
        Issue savedIssue = anIssue().withId(ISSUE_ID_1).build();

        when(issueRepository.save(any(Issue.class))).thenReturn(savedIssue);

        IssueResponseDto result = issueService.createIssue(dto);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("createIssue - should return issue with correct ID")
    void createIssue_shouldReturnIssueWithCorrectId() {
        IssueCreationDto dto = anIssueCreationDto().build();
        Issue savedIssue = anIssue().withId(ISSUE_ID_1).build();

        when(issueRepository.save(any(Issue.class))).thenReturn(savedIssue);

        IssueResponseDto result = issueService.createIssue(dto);

        assertThat(result.getId()).isEqualTo(ISSUE_ID_1);
    }

    // ========== getIssue() tests ==========

    @Test
    @DisplayName("getIssue - should call findByIdAndIsActiveTrue")
    void getIssue_shouldCallFindByIdAndIsActiveTrue() {
        Issue issue = anIssue().withId(ISSUE_ID_1).build();

        when(issueRepository.findByIdAndIsActiveTrue(ISSUE_ID_1)).thenReturn(Optional.of(issue));

        issueService.getIssue(ISSUE_ID_1);

        verify(issueRepository).findByIdAndIsActiveTrue(ISSUE_ID_1);
    }

    @Test
    @DisplayName("getIssue - should return issue when found")
    void getIssue_shouldReturnIssueWhenFound() {
        Issue issue = anIssue().withId(ISSUE_ID_1).build();

        when(issueRepository.findByIdAndIsActiveTrue(ISSUE_ID_1)).thenReturn(Optional.of(issue));

        IssueResponseDto result = issueService.getIssue(ISSUE_ID_1);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("getIssue - should return issue with correct ID")
    void getIssue_shouldReturnIssueWithCorrectId() {
        Issue issue = anIssue().withId(ISSUE_ID_1).build();

        when(issueRepository.findByIdAndIsActiveTrue(ISSUE_ID_1)).thenReturn(Optional.of(issue));

        IssueResponseDto result = issueService.getIssue(ISSUE_ID_1);

        assertThat(result.getId()).isEqualTo(ISSUE_ID_1);
    }

    @Test
    @DisplayName("getIssue - should throw when issue not found")
    void getIssue_shouldThrowWhenIssueNotFound() {
        when(issueRepository.findByIdAndIsActiveTrue(NON_EXISTENT_ISSUE_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> issueService.getIssue(NON_EXISTENT_ISSUE_ID))
                .isInstanceOf(HandledException.class)
                .hasFieldOrPropertyWithValue("errorMessage", ErrorMessage.ISSUE_NOT_FOUND);
    }

    // ========== updateIssue() tests ==========

    @Test
    @DisplayName("updateIssue - should call findByIdAndIsActiveTrue")
    void updateIssue_shouldCallFindByIdAndIsActiveTrue() {
        Issue existingIssue = anIssue().withId(ISSUE_ID_1).build();
        IssueUpdateDto dto = anIssueUpdateDto().build();
        Issue updatedIssue = anIssue().build();

        when(issueRepository.findByIdAndIsActiveTrue(ISSUE_ID_1)).thenReturn(Optional.of(existingIssue));
        when(issueRepository.save(any(Issue.class))).thenReturn(updatedIssue);

        issueService.updateIssue(dto, ISSUE_ID_1);

        verify(issueRepository).findByIdAndIsActiveTrue(ISSUE_ID_1);
    }

    @Test
    @DisplayName("updateIssue - should throw when issue not found")
    void updateIssue_shouldThrowWhenIssueNotFound() {
        IssueUpdateDto dto = anIssueUpdateDto().build();

        when(issueRepository.findByIdAndIsActiveTrue(NON_EXISTENT_ISSUE_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> issueService.updateIssue(dto, NON_EXISTENT_ISSUE_ID))
                .isInstanceOf(HandledException.class)
                .hasFieldOrPropertyWithValue("errorMessage", ErrorMessage.ISSUE_NOT_FOUND);
    }

    @Test
    @DisplayName("updateIssue - should not call save when issue not found")
    void updateIssue_shouldNotCallSaveWhenIssueNotFound() {
        IssueUpdateDto dto = anIssueUpdateDto().build();

        when(issueRepository.findByIdAndIsActiveTrue(NON_EXISTENT_ISSUE_ID)).thenReturn(Optional.empty());

        try {
            issueService.updateIssue(dto, NON_EXISTENT_ISSUE_ID);
        } catch (HandledException e) {
            // Expected
        }

        verify(issueRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateIssue - should validate criticality ID")
    void updateIssue_shouldValidateCriticalityId() {
        Issue existingIssue = anIssue().withId(ISSUE_ID_1).build();
        IssueUpdateDto dto = anIssueUpdateDto()
                .withCriticalityId(INVALID_ISSUE_CRITICALITY_ID)
                .build();

        when(issueRepository.findByIdAndIsActiveTrue(ISSUE_ID_1)).thenReturn(Optional.of(existingIssue));

        assertThatThrownBy(() -> issueService.updateIssue(dto, ISSUE_ID_1))
                .isInstanceOf(HandledException.class)
                .hasFieldOrPropertyWithValue("errorMessage", ErrorMessage.ISSUE_CRITICALITY_NOT_FOUND);
    }

    @Test
    @DisplayName("updateIssue - should not call save when criticality invalid")
    void updateIssue_shouldNotCallSaveWhenCriticalityInvalid() {
        Issue existingIssue = anIssue().withId(ISSUE_ID_1).build();
        IssueUpdateDto dto = anIssueUpdateDto()
                .withCriticalityId(INVALID_ISSUE_CRITICALITY_ID)
                .build();

        when(issueRepository.findByIdAndIsActiveTrue(ISSUE_ID_1)).thenReturn(Optional.of(existingIssue));

        try {
            issueService.updateIssue(dto, ISSUE_ID_1);
        } catch (HandledException e) {
            // Expected
        }

        verify(issueRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateIssue - should validate status ID")
    void updateIssue_shouldValidateStatusId() {
        Issue existingIssue = anIssue().withId(ISSUE_ID_1).build();
        IssueUpdateDto dto = anIssueUpdateDto()
                .withStatusId(INVALID_ISSUE_STATUS_ID)
                .build();

        when(issueRepository.findByIdAndIsActiveTrue(ISSUE_ID_1)).thenReturn(Optional.of(existingIssue));

        assertThatThrownBy(() -> issueService.updateIssue(dto, ISSUE_ID_1))
                .isInstanceOf(HandledException.class)
                .hasFieldOrPropertyWithValue("errorMessage", ErrorMessage.ISSUE_STATUS_NOT_FOUND);
    }

    @Test
    @DisplayName("updateIssue - should not call save when status invalid")
    void updateIssue_shouldNotCallSaveWhenStatusInvalid() {
        Issue existingIssue = anIssue().withId(ISSUE_ID_1).build();
        IssueUpdateDto dto = anIssueUpdateDto()
                .withStatusId(INVALID_ISSUE_STATUS_ID)
                .build();

        when(issueRepository.findByIdAndIsActiveTrue(ISSUE_ID_1)).thenReturn(Optional.of(existingIssue));

        try {
            issueService.updateIssue(dto, ISSUE_ID_1);
        } catch (HandledException e) {
            // Expected
        }

        verify(issueRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateIssue - should call save")
    void updateIssue_shouldCallSave() {
        Issue existingIssue = anIssue().withId(ISSUE_ID_1).build();
        IssueUpdateDto dto = anIssueUpdateDto().build();
        Issue updatedIssue = anIssue().build();

        when(issueRepository.findByIdAndIsActiveTrue(ISSUE_ID_1)).thenReturn(Optional.of(existingIssue));
        when(issueRepository.save(any(Issue.class))).thenReturn(updatedIssue);

        issueService.updateIssue(dto, ISSUE_ID_1);

        verify(issueRepository).save(any(Issue.class));
    }

    @Test
    @DisplayName("updateIssue - should return updated issue")
    void updateIssue_shouldReturnUpdatedIssue() {
        Issue existingIssue = anIssue().withId(ISSUE_ID_1).build();
        IssueUpdateDto dto = anIssueUpdateDto().build();
        Issue updatedIssue = anIssue().withId(ISSUE_ID_1).build();

        when(issueRepository.findByIdAndIsActiveTrue(ISSUE_ID_1)).thenReturn(Optional.of(existingIssue));
        when(issueRepository.save(any(Issue.class))).thenReturn(updatedIssue);

        IssueResponseDto result = issueService.updateIssue(dto, ISSUE_ID_1);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("updateIssue - should return issue with correct ID")
    void updateIssue_shouldReturnIssueWithCorrectId() {
        Issue existingIssue = anIssue().withId(ISSUE_ID_1).build();
        IssueUpdateDto dto = anIssueUpdateDto().build();
        Issue updatedIssue = anIssue().withId(ISSUE_ID_1).build();

        when(issueRepository.findByIdAndIsActiveTrue(ISSUE_ID_1)).thenReturn(Optional.of(existingIssue));
        when(issueRepository.save(any(Issue.class))).thenReturn(updatedIssue);

        IssueResponseDto result = issueService.updateIssue(dto, ISSUE_ID_1);

        assertThat(result.getId()).isEqualTo(ISSUE_ID_1);
    }

    // ========== deleteIssue() tests ==========

    @Test
    @DisplayName("deleteIssue - should throw when issue not found")
    void deleteIssue_shouldThrowWhenIssueNotFound() {
        when(issueRepository.findByIdAndIsActiveTrue(NON_EXISTENT_ISSUE_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> issueService.deleteIssue(NON_EXISTENT_ISSUE_ID))
                .isInstanceOf(HandledException.class)
                .hasFieldOrPropertyWithValue("errorMessage", ErrorMessage.ISSUE_NOT_FOUND);
    }

    @Test
    @DisplayName("deleteIssue - should not call deactivate when issue not found")
    void deleteIssue_shouldNotCallDeactivateWhenIssueNotFound() {
        when(issueRepository.findByIdAndIsActiveTrue(NON_EXISTENT_ISSUE_ID)).thenReturn(Optional.empty());

        try {
            issueService.deleteIssue(NON_EXISTENT_ISSUE_ID);
        } catch (HandledException e) {
            // Expected
        }

        verify(issueRepository, never()).deactivateIssue(any());
    }

    @Test
    @DisplayName("deleteIssue - should call deactivateIssue")
    void deleteIssue_shouldCallDeactivateIssue() {
        Issue existingIssue = anIssue().withId(ISSUE_ID_1).build();

        when(issueRepository.findByIdAndIsActiveTrue(ISSUE_ID_1)).thenReturn(Optional.of(existingIssue));

        issueService.deleteIssue(ISSUE_ID_1);

        verify(issueRepository).deactivateIssue(ISSUE_ID_1);
    }

    // ========== deactivateIssuesByAncestorTaskId() tests ==========

    @Test
    @DisplayName("deactivateIssuesByAncestorTaskId - should call repository method")
    void deactivateIssuesByAncestorTaskId_shouldCallRepositoryMethod() {
        issueService.deactivateIssuesByAncestorTaskId(TASK_ID_1);

        verify(issueRepository).deactivateIssuesByAncestorTaskId(TASK_ID_1);
    }
}
