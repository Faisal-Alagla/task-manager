package com.faisal.taskmanager.issue;

import com.faisal.taskmanager.common.exceptions.ErrorMessage;
import com.faisal.taskmanager.common.exceptions.HandledException;
import com.faisal.taskmanager.issue.dto.IssueCreationDto;
import com.faisal.taskmanager.issue.dto.IssueResponseDto;
import com.faisal.taskmanager.issue.dto.IssueUpdateDto;
import com.faisal.taskmanager.issue.validator.IssueValidator;
import com.faisal.taskmanager.task.validator.TaskValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IssueService implements IIssueService {

    private final IssueRepository issueRepository;
    private final TaskValidator taskValidator;
    private final IssueValidator issueValidator;

    @Override
    public IssueResponseDto createIssue(IssueCreationDto issueCreationDto) {
        taskValidator.validateTaskExists(issueCreationDto.getTaskId(), "taskId");
        issueValidator.validateIssueCreationLookups(issueCreationDto.getCriticalityId(), issueCreationDto.getStatusId());

        Issue createdIssue = issueRepository.save(IssueMapper.mapToIssue(issueCreationDto));

        return IssueMapper.mapToIssueResponseDto(createdIssue);
    }

    @Override
    public IssueResponseDto getIssue(UUID issueId) {
        Issue issue = issueRepository.findByIdAndIsActiveTrue(issueId)
                .orElseThrow(() -> new HandledException(ErrorMessage.ISSUE_NOT_FOUND));

        return IssueMapper.mapToIssueResponseDto(issue);
    }

    @Override
    public IssueResponseDto updateIssue(IssueUpdateDto issueUpdateDto, UUID issueId) {
        Issue issue = issueRepository.findByIdAndIsActiveTrue(issueId)
                .orElseThrow(() -> new HandledException(ErrorMessage.ISSUE_NOT_FOUND));

        issueValidator.validateIssueUpdateLookups(issueUpdateDto.getCriticalityId(), issueUpdateDto.getStatusId());

        updateIssueData(issue, issueUpdateDto);
        Issue updatedIssue = issueRepository.save(issue);

        return IssueMapper.mapToIssueResponseDto(updatedIssue);
    }

    @Override
    public void deleteIssue(UUID issueId) {
        if (!issueExists(issueId)) {
            throw new HandledException(ErrorMessage.ISSUE_NOT_FOUND);
        }

        issueRepository.deactivateIssue(issueId);
    }

    @Override
    public void deactivateIssuesByAncestorTaskId(UUID ancestorTaskId) {
        issueRepository.deactivateIssuesByAncestorTaskId(ancestorTaskId);
    }

    private boolean issueExists(UUID issueId) {
        return issueRepository.findByIdAndIsActiveTrue(issueId).isPresent();
    }

    private void updateIssueData(Issue issue, IssueUpdateDto issueUpdateDto) {
        issue.setUpdatedAt(Instant.now());
        issue.setName(issueUpdateDto.getName());
        issue.setDescription(issueUpdateDto.getDescription());
        issue.setStatusId(issueUpdateDto.getStatusId());
        issue.setCriticalityId(issueUpdateDto.getCriticalityId());
    }

}