package com.faisal.taskmanager.common.lookups.specifications;

import com.faisal.taskmanager.common.lookups.entities.TaskStatusLk;
import com.faisal.taskmanager.common.lookups.enums.TaskStatusEnum;

import java.util.Set;

public class TaskStatusSpecifications implements CommonSpecifications<TaskStatusLk> {

    private final Set<TaskStatusEnum> TERMINAL_STATUS = Set.of(
            TaskStatusEnum.COMPLETED,
            TaskStatusEnum.CANCELLED
    );

    public LookupSpecification<TaskStatusLk> isTerminal() {
        return () -> lookup -> TERMINAL_STATUS.stream()
                .anyMatch(status -> lookup.getId().equals(status.getId()));
    }

}