package com.faisal.taskmanager.common.lookups;

import com.faisal.taskmanager.common.lookups.domain.IssueCriticalityLookupCollection;
import com.faisal.taskmanager.common.lookups.domain.IssueStatusLookupCollection;
import com.faisal.taskmanager.common.lookups.domain.TaskPriorityLookupCollection;
import com.faisal.taskmanager.common.lookups.domain.TaskStatusLookupCollection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LookupService {

    private final LookupRepository lookupRepository;

    private IssueCriticalityLookupCollection issueCriticalities;
    private IssueStatusLookupCollection issueStatuses;
    private TaskPriorityLookupCollection taskPriorities;
    private TaskStatusLookupCollection taskStatuses;

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @EventListener(ApplicationReadyEvent.class)
    protected void initializeLookups() {
        log.info("Initializing lookup collections...");

        issueCriticalities = new IssueCriticalityLookupCollection(
                lookupRepository.getIssueCriticalityLookup()
        );

        issueStatuses = new IssueStatusLookupCollection(
                lookupRepository.getIssueStatusLookup()
        );

        taskPriorities = new TaskPriorityLookupCollection(
                lookupRepository.getTaskPriorityLookup()
        );

        taskStatuses = new TaskStatusLookupCollection(
                lookupRepository.getTaskStatusLookup()
        );
    }

    // Controller endpoints
    public List<LookupResponseDto> getIssueCriticalities() {
        return issueCriticalities.toDtoList();
    }

    public List<LookupResponseDto> getIssueStatuses() {
        return issueStatuses.toDtoList();
    }

    public List<LookupResponseDto> getTaskPriorities() {
        return taskPriorities.toDtoList();
    }

    public List<LookupResponseDto> getTaskStatuses() {
        return taskStatuses.toDtoList();
    }

    // Public API for other services
    public IssueCriticalityLookupCollection getIssueCriticalityCollection() {
        return issueCriticalities;
    }

    public IssueStatusLookupCollection getIssueStatusCollection() {
        return issueStatuses;
    }

    public TaskPriorityLookupCollection getTaskPriorityCollection() {
        return taskPriorities;
    }

    public TaskStatusLookupCollection getTaskStatusCollection() {
        return taskStatuses;
    }
}