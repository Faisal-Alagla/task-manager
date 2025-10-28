package com.faisal.taskmanager.issue;


import com.faisal.taskmanager.issue.dto.IssueCreationDto;
import com.faisal.taskmanager.issue.dto.IssueResponseDto;

public class IssueMapper {

    /**
     * maps an Issue object to IssueResponseDto
     *
     * @param issue The Issue object
     * @return IssueResponseDto object containing the passed Issue data
     */
    public static IssueResponseDto mapToIssueResponseDto(Issue issue) {
        return IssueResponseDto.builder()
                .id(issue.getId())
                .name(issue.getName())
                .description(issue.getDescription())
                .taskId(issue.getTaskId())
                .statusId(issue.getStatusId())
                .criticalityId(issue.getCriticalityId())
                .build();
    }

    /**
     * maps a IssueCreationDto object to Issue
     *
     * @param issueCreationDto The IssueCreationDto object
     * @return Issue object containing the passed IssueCreationDto data
     */
    public static Issue mapToIssue(IssueCreationDto issueCreationDto) {
        return Issue.builder()
                .name(issueCreationDto.getName())
                .description(issueCreationDto.getDescription())
                .taskId(issueCreationDto.getTaskId())
                .statusId(issueCreationDto.getStatusId())
                .criticalityId(issueCreationDto.getCriticalityId())
                .build();
    }

}
