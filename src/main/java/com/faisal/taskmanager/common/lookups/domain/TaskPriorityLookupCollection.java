package com.faisal.taskmanager.common.lookups.domain;

import com.faisal.taskmanager.common.lookups.entities.TaskPriorityLk;
import com.faisal.taskmanager.common.lookups.enums.TaskPriorityEnum;
import com.faisal.taskmanager.common.lookups.specifications.TaskPrioritySpecifications;

import java.util.List;

public class TaskPriorityLookupCollection extends BaseLookupCollection<TaskPriorityLk> {

    public TaskPriorityLookupCollection(List<TaskPriorityLk> items) {
        super(items);
    }

    /**
     * Convert entity ID to enum
     */
    public TaskPriorityEnum toEnum(Integer priorityId) {
        return TaskPriorityEnum.fromId(priorityId).orElse(null);
    }

    /**
     * Get entity by enum
     */
    public TaskPriorityLk getByEnum(TaskPriorityEnum priorityEnum) {
        return findById(priorityEnum.getId());
    }

    /**
     * Get high priority
     */
    public List<TaskPriorityLk> getHighPriorities() {
        return findBy(TaskPrioritySpecifications.isHigh());
    }

    /**
     * Get low priorities
     */
    public List<TaskPriorityLk> getLowPriorities() {
        return findBy(TaskPrioritySpecifications.isLow());
    }

    /**
     * Get medium priorities
     */
    public List<TaskPriorityLk> getMediumPriorities() {
        return findBy(TaskPrioritySpecifications.isMedium());
    }

    /**
     * Check if priority is high
     */
    public boolean isHigh(Integer priorityId) {
        return TaskPriorityEnum.fromId(priorityId)
                .map(TaskPriorityEnum::isHigh)
                .orElse(false);
    }

    /**
     * Check if priority is low
     */
    public boolean isLow(Integer priorityId) {
        return TaskPriorityEnum.fromId(priorityId)
                .map(TaskPriorityEnum::isLow)
                .orElse(false);
    }

    /**
     * Check if priority is higher than another
     */
    public boolean isHigherThan(Integer priorityId, Integer otherPriorityId) {
        TaskPriorityEnum priority = TaskPriorityEnum.fromId(priorityId).orElse(null);
        TaskPriorityEnum other = TaskPriorityEnum.fromId(otherPriorityId).orElse(null);

        if (priority == null || other == null) {
            return false;
        }

        return priority.isHigherThan(other);
    }

    /**
     * Get priority level (1-3, higher is more important)
     */
    public int getPriorityLevel(Integer priorityId) {
        return TaskPriorityEnum.fromId(priorityId)
                .map(TaskPriorityEnum::getPriorityLevel)
                .orElse(0);
    }
}