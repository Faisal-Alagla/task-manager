package com.faisal.taskmanager.task;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

/**
 * data container for lookup IDs and mappings.
 */
@Getter
@Builder
public class TaskLookupContext {

    // Status IDs
    private Set<Integer> taskStatusIds;
    private Set<Integer> terminalStatusIds;

    // Priority IDs
    private Set<Integer> taskPriorityIds;

}
