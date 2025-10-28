package com.faisal.taskmanager.common.lookups.domain;

import com.faisal.taskmanager.common.lookups.entities.TaskStatusLk;
import com.faisal.taskmanager.common.lookups.specifications.TaskStatusSpecifications;

import java.util.List;

public class TaskStatusLookupCollection extends BaseLookupCollection<TaskStatusLk> {

    private final TaskStatusSpecifications taskStatusSpecifications = new TaskStatusSpecifications();

    public TaskStatusLookupCollection(List<TaskStatusLk> items) {
        super(items);
    }

    public List<TaskStatusLk> getTerminalStatuses() {
        return filter(taskStatusSpecifications.isTerminal());
    }

}