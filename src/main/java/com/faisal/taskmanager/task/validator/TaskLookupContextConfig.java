package com.faisal.taskmanager.task.validator;

import com.faisal.taskmanager.common.lookups.LookupService;
import com.faisal.taskmanager.common.lookups.domain.TaskPriorityLookupCollection;
import com.faisal.taskmanager.common.lookups.domain.TaskStatusLookupCollection;
import com.faisal.taskmanager.common.lookups.entities.TaskPriorityLk;
import com.faisal.taskmanager.common.lookups.entities.TaskStatusLk;
import com.faisal.taskmanager.task.TaskLookupContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Collectors;

/**
 * Configuration class for creating TaskLookupContext bean.
 *
 * <p>Extracts lookup IDs from collections and provides them as an immutable context
 * for validation and business logic operations.</p>
 *
 * @author Faisal
 */
@Configuration
@RequiredArgsConstructor
public class TaskLookupContextConfig {

    private final LookupService lookupService;

    /**
     * Creates TaskLookupContext bean with pre-extracted lookup IDs.
     *
     * @return immutable TaskLookupContext for validation operations
     */
    @Bean
    public TaskLookupContext taskLookupContext() {
        TaskStatusLookupCollection statuses = lookupService.getTaskStatusCollection();
        TaskPriorityLookupCollection priorities = lookupService.getTaskPriorityCollection();

        return TaskLookupContext.builder()
                // Task status IDs
                .taskStatusIds(
                        statuses.stream()
                                .map(TaskStatusLk::getId)
                                .collect(Collectors.toSet())
                )
                .terminalStatusIds(
                        statuses.getTerminalStatuses().stream()
                                .map(TaskStatusLk::getId)
                                .collect(Collectors.toSet())
                )

                // Task priority IDs
                .taskPriorityIds(
                        priorities.stream()
                                .map(TaskPriorityLk::getId)
                                .collect(Collectors.toSet())
                )

                .build();
    }
}
