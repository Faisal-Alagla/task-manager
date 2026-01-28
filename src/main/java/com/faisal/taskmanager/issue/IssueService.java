package com.faisal.taskmanager.issue;

import com.faisal.taskmanager.common.exceptions.ErrorMessage;
import com.faisal.taskmanager.common.exceptions.HandledException;
import com.faisal.taskmanager.common.lookups.LookupService;
import com.faisal.taskmanager.common.lookups.domain.IssueCriticalityLookupCollection;
import com.faisal.taskmanager.common.lookups.domain.IssueStatusLookupCollection;
import com.faisal.taskmanager.common.lookups.entities.IssueCriticalityLk;
import com.faisal.taskmanager.common.lookups.entities.IssueStatusLk;
import com.faisal.taskmanager.issue.dto.IssueCreationDto;
import com.faisal.taskmanager.issue.dto.IssueResponseDto;
import com.faisal.taskmanager.issue.dto.IssueUpdateDto;
import com.faisal.taskmanager.task.TaskValidator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IssueService implements IIssueService {

    private final IssueRepository issueRepository;
    private final LookupService lookupService;
    private final TaskValidator taskValidator;
    private final IssueValidator issueValidator;

    // lookup collections
    private IssueCriticalityLookupCollection criticalities;
    private IssueStatusLookupCollection statuses;

    // lookup context
    private IssueLookupContext issueLookupContext;

    @PostConstruct
    private void init() {
        this.criticalities = lookupService.getIssueCriticalityCollection();
        this.statuses = lookupService.getIssueStatusCollection();

        this.issueLookupContext = buildLookupContext();

        // Inject context into validator
        issueValidator.setIssueLookupContext(issueLookupContext);
    }

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

    private IssueLookupContext buildLookupContext() {
        return IssueLookupContext.builder()
                // Issue status IDs
                .issueStatusIds(
                        statuses.stream()
                                .map(IssueStatusLk::getId)
                                .collect(Collectors.toSet())
                )

                // Issue criticality IDs
                .issueCriticalityIds(
                        criticalities.stream()
                                .map(IssueCriticalityLk::getId)
                                .collect(Collectors.toSet())
                )

                .build();
    }

}