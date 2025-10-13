package com.faisal.taskmanager.issue;

import com.faisal.taskmanager.common.exceptions.ErrorMessage;
import com.faisal.taskmanager.common.exceptions.ResourceException;
import com.faisal.taskmanager.common.lookups.LookupService;
import com.faisal.taskmanager.common.lookups.domain.IssueCriticalityLookupCollection;
import com.faisal.taskmanager.common.lookups.domain.IssueStatusLookupCollection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IssueService implements IIssueService {

    private final IssueRepository issueRepository;
    private final LookupService lookupService;

    // Cached lookup collections
    private IssueCriticalityLookupCollection criticalities;
    private IssueStatusLookupCollection statuses;

    @PostConstruct
    private void init() {
        this.criticalities = lookupService.getIssueCriticalityCollection();
        this.statuses = lookupService.getIssueStatusCollection();
    }

    @Override
    public IssueResponseDto createIssue(IssueCreationDto issueCreationDto) {
        validateIssueLookups(issueCreationDto.getCriticalityId(), issueCreationDto.getStatusId());

        Issue createdIssue = issueRepository.save(IssueMapper.mapToIssue(issueCreationDto));

        return IssueMapper.mapToIssueResponseDto(createdIssue);
    }

    @Override
    public IssueResponseDto getIssue(UUID issueId) {
        Issue issue = issueRepository.findByIdAndIsActiveTrue(issueId)
                .orElseThrow(() -> new ResourceException(ErrorMessage.ISSUE_NOT_FOUND));

        return IssueMapper.mapToIssueResponseDto(issue);
    }

    @Override
    public IssueResponseDto updateIssue(IssueUpdateDto issueUpdateDto, UUID issueId) {
        Issue issue = issueRepository.findByIdAndIsActiveTrue(issueId)
                .orElseThrow(() -> new ResourceException(ErrorMessage.ISSUE_NOT_FOUND));

        validateIssueLookups(issueUpdateDto.getCriticalityId(), issueUpdateDto.getStatusId());

        updateIssueData(issue, issueUpdateDto);
        Issue updatedIssue = issueRepository.save(issue);

        return IssueMapper.mapToIssueResponseDto(updatedIssue);
    }

    @Override
    public void deleteIssue(UUID issueId) {
        if (!issueExists(issueId)) {
            throw new ResourceException(ErrorMessage.ISSUE_NOT_FOUND);
        }

        issueRepository.deactivateIssue(issueId);
    }

    private boolean issueExists(UUID issueId) {
        return issueRepository.findByIdAndIsActiveTrue(issueId).isPresent();
    }

    private void updateIssueData(Issue issue, IssueUpdateDto issueUpdateDto) {
        issue.setUpdatedAt(LocalDateTime.now());
        issue.setName(issueUpdateDto.getName());
        issue.setDescription(issueUpdateDto.getDescription());
        issue.setStatusId(issueUpdateDto.getStatusId());
        issue.setCriticalityId(issueUpdateDto.getCriticalityId());
    }

    private void validateIssueLookups(Integer criticalityId, Integer statusId) {
        if (!criticalities.containsId(criticalityId)) {
            throw new ResourceException(ErrorMessage.ISSUE_CRITICALITY_NOT_FOUND);
        }

        if (!statuses.containsId(statusId)) {
            throw new ResourceException(ErrorMessage.ISSUE_STATUS_NOT_FOUND);
        }
    }

}