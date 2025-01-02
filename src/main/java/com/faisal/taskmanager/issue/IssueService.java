package com.faisal.taskmanager.issue;

import com.faisal.taskmanager.task.Task;
import com.faisal.taskmanager.task.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IssueService implements IIssueService {

    private final IssueRepository issueRepository;
    private final TaskRepository taskRepository;

    @Override
    public IssueResponseDto createIssue(IssueCreationDto issueCreationDto) {
        Task task = taskRepository.findById(issueCreationDto.getTaskId())
                .orElseThrow(RuntimeException::new); //TODO: to be changed with custom exception

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
    public void deleteIssue(UUID issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(RuntimeException::new); //TODO: to be changed with custom exception

        issueRepository.deleteById(issueId);
    }

}
