package com.faisal.taskmanager.issue;

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
        Issue createdIssue = issueRepository.save(IssueMapper.mapToIssue(issueCreationDto));

        return IssueMapper.mapToIssueResponseDto(createdIssue);
    }

    @Override
    public IssueResponseDto getIssue(UUID issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(RuntimeException::new); //TODO: to be changed with custom exception

        return IssueMapper.mapToIssueResponseDto(issue);
    }

    @Override
    public IssueResponseDto updateIssue(IssueUpdateDto issueUpdateDto, UUID issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(RuntimeException::new); //TODO: to be changed with custom exception

        updateIssueData(issue, issueUpdateDto);
        Issue updatedIssue = issueRepository.save(issue);

        return IssueMapper.mapToIssueResponseDto(updatedIssue);
    }

    @Override
    public void deleteIssue(UUID issueId) {
        issueRepository.findById(issueId)
                .orElseThrow(RuntimeException::new); //TODO: to be changed with custom exception

        issueRepository.deleteById(issueId);
    }

    private void updateIssueData(Issue issue, IssueUpdateDto issueUpdateDto) {
        issue.setUpdatedAt(LocalDateTime.now());
        issue.setName(issueUpdateDto.getName());
        issue.setDescription(issueUpdateDto.getDescription());
        issue.setStatusId(issueUpdateDto.getStatusId());
        issue.setCriticalityId(issueUpdateDto.getCriticalityId());
    }

}
