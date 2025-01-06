package com.faisal.taskmanager.issue;

import com.faisal.taskmanager.common.exceptions.ErrorMessage;
import com.faisal.taskmanager.common.exceptions.ResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IssueService implements IIssueService {

    private final IssueRepository issueRepository;

    @Override
    public IssueResponseDto createIssue(IssueCreationDto issueCreationDto) {
        //TODO: check if task is active first (validation on taskId)
        Issue createdIssue = issueRepository.save(IssueMapper.mapToIssue(issueCreationDto));

        return IssueMapper.mapToIssueResponseDto(createdIssue);
    }

    @Override
    public IssueResponseDto getIssue(UUID issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new ResourceException(ErrorMessage.ISSUE_NOT_FOUND));

        return IssueMapper.mapToIssueResponseDto(issue);
    }

    @Override
    public IssueResponseDto updateIssue(IssueUpdateDto issueUpdateDto, UUID issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new ResourceException(ErrorMessage.ISSUE_NOT_FOUND));

        updateIssueData(issue, issueUpdateDto);
        Issue updatedIssue = issueRepository.save(issue);

        return IssueMapper.mapToIssueResponseDto(updatedIssue);
    }

    @Override
    public void deleteIssue(UUID issueId) {
        issueRepository.findById(issueId)
                .orElseThrow(() -> new ResourceException(ErrorMessage.ISSUE_NOT_FOUND));

        issueRepository.deactivateIssue(issueId);
    }

    private void updateIssueData(Issue issue, IssueUpdateDto issueUpdateDto) {
        issue.setUpdatedAt(LocalDateTime.now());
        issue.setName(issueUpdateDto.getName());
        issue.setDescription(issueUpdateDto.getDescription());
        issue.setStatusId(issueUpdateDto.getStatusId());
        issue.setCriticalityId(issueUpdateDto.getCriticalityId());
    }

}
