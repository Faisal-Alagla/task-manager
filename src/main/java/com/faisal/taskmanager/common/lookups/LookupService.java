package com.faisal.taskmanager.common.lookups;

import com.faisal.taskmanager.common.lookups.entities.IssueCriticalityLk;
import com.faisal.taskmanager.common.lookups.entities.IssueStatusLk;
import com.faisal.taskmanager.common.lookups.entities.TaskPriorityLk;
import com.faisal.taskmanager.common.lookups.entities.TaskStatusLk;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class LookupService {

    private final LookupRepository lookupRepository;

    private List<IssueCriticalityLk> issueCriticalityLookupList = List.of();
    private List<IssueStatusLk> issueStatusLookupList = List.of();
    private List<TaskPriorityLk> taskPriorityLookupList = List.of();
    private List<TaskStatusLk> taskStatusLookupList = List.of();

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @EventListener(ApplicationReadyEvent.class)
    protected void initializeLookups() {
        log.info("filling lookup values");

        issueCriticalityLookupList = lookupRepository.getIssueCriticalityLookup();
        issueStatusLookupList = lookupRepository.getIssueStatusLookup();
        taskPriorityLookupList = lookupRepository.getTaskPriorityLookup();
        taskStatusLookupList = lookupRepository.getTaskStatusLookup();
    }

    Stream<LookupResponseDto> getLookup(LookupType type) {

        switch (type) {
            case ISSUE_CRITICALITY -> {
                return issueCriticalityLookupList.stream().map(LookupResponseDto::fromEntity);
            }
            case ISSUE_STATUS -> {
                return issueStatusLookupList.stream().map(LookupResponseDto::fromEntity);
            }
            case TASK_PRIORITY -> {
                return taskPriorityLookupList.stream().map(LookupResponseDto::fromEntity);
            }
            case TASK_STATUS -> {
                return taskStatusLookupList.stream().map(LookupResponseDto::fromEntity);
            }
            default -> {
                log.error("no values for the given lookup type");
                return null;
            }
        }

    }

    public Optional<LookupResponseDto> findLookupById(LookupType type, Integer id) {
        return getLookup(type)
                .filter(item -> item.getId().equals(id))
                .findFirst();
    }

    public Optional<LookupResponseDto> findLookupByName(LookupType type, String name) {
        return getLookup(type)
                .filter(item -> item.getName().equals(name))
                .findFirst();
    }

    public List<LookupResponseDto> findLookupByNames(LookupType type, List<String> names) {
        return getLookup(type)
                .filter(item -> names.contains(item.getName()))
                .toList();
    }

}
