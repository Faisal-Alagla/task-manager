package com.faisal.taskmanager.common.lookups.domain;

import com.faisal.taskmanager.common.lookups.entities.TaskPriorityLk;

import java.util.List;

public class TaskPriorityLookupCollection extends BaseLookupCollection<TaskPriorityLk> {

    public TaskPriorityLookupCollection(List<TaskPriorityLk> items) {
        super(items);
    }

}