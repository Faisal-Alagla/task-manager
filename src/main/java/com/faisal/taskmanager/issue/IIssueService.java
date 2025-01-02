package com.faisal.taskmanager.issue;

import java.util.UUID;

public interface IIssueService {

    /**
     * Creates a new Issue and store it in the DB
     *
     * @param issueCreationDto IssueCreationDto object containing the Issue data to be created
     * @return IssueResponseDto containing the created Issue data in the DB
     */
    IssueResponseDto createIssue(IssueCreationDto issueCreationDto);

    /**
     * Gets the Issue from the DB with the matching Issue id
     *
     * @param issueId id of the Issue to be fetched
     * @return IssueResponseDto containing the Issue data
     */
    IssueResponseDto getIssue(UUID issueId);

    /**
     * Soft Deletes an Issue in the DB with the matching Issue id
     *
     * @param issueId id of the Issue to be deleted
     */
    void deleteIssue(UUID issueId);

}
