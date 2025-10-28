package com.faisal.taskmanager.issue;

import com.faisal.taskmanager.issue.dto.IssueCreationDto;
import com.faisal.taskmanager.issue.dto.IssueResponseDto;
import com.faisal.taskmanager.issue.dto.IssueUpdateDto;

import java.util.UUID;

public interface IIssueService {

    /**
     * Creates a new issue
     *
     * @param issueCreationDto IssueCreationDto object containing the issue data to be created
     * @return IssueResponseDto containing the created issue data
     */
    IssueResponseDto createIssue(IssueCreationDto issueCreationDto);

    /**
     * Gets an issue by id
     *
     * @param issueId id of the issue to be fetched
     * @return IssueResponseDto containing the issue data
     */
    IssueResponseDto getIssue(UUID issueId);

    /**
     * Update an issue by id
     *
     * @param issueUpdateDto IssueUpdateDto object containing the new issue data
     * @param issueId  id of the issue to be updated
     * @return IssueResponseDto object containing the updated issue data
     */
    IssueResponseDto updateIssue(IssueUpdateDto issueUpdateDto, UUID issueId);

    /**
     * Soft delete an issue by id
     *
     * @param issueId id of the Issue to be deleted
     */
    void deleteIssue(UUID issueId);

}
