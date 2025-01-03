package com.faisal.taskmanager.issue;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IssueService implements IIssueService {

    private final IssueRepository issueRepository;

    @Override
    public IssueResponseDto createIssue(IssueCreationDto issueCreationDto) {
        try {
            Issue createdIssue = issueRepository.save(IssueMapper.mapToIssue(issueCreationDto));
            return IssueMapper.mapToIssueResponseDto(createdIssue);
        } catch (DataIntegrityViolationException ex) {
            //TODO: to be changed with custom exception
            throw ex;
        }
    }

    @Override
    public IssueResponseDto getIssue(UUID issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(RuntimeException::new); //TODO: to be changed with custom exception

        return IssueMapper.mapToIssueResponseDto(issue);
    }

    @Override
    public void deleteIssue(UUID issueId) {
        issueRepository.findById(issueId)
                .orElseThrow(RuntimeException::new); //TODO: to be changed with custom exception

        issueRepository.deleteById(issueId);
    }

}
