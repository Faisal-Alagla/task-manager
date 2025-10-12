package com.faisal.taskmanager.common.lookups.specifications;

import com.faisal.taskmanager.common.lookups.entities.TaskPriorityLk;
import com.faisal.taskmanager.common.lookups.enums.TaskPriorityEnum;

public class TaskPrioritySpecifications {

    /**
     * Specification for high priority
     */
    public static LookupSpecification<TaskPriorityLk> isHigh() {
        return () -> priority -> TaskPriorityEnum.fromId(priority.getId())
                    .map(TaskPriorityEnum::isHigh)
                    .orElse(false);
    }

    /**
     * Specification for low priority
     */
    public static LookupSpecification<TaskPriorityLk> isLow() {
        return () -> priority -> TaskPriorityEnum.fromId(priority.getId())
                    .map(TaskPriorityEnum::isLow)
                    .orElse(false);
    }

    /**
     * Specification for medium priority
     */
    public static LookupSpecification<TaskPriorityLk> isMedium() {
        return () -> priority -> TaskPriorityEnum.fromId(priority.getId())
                    .map(e -> e == TaskPriorityEnum.MEDIUM)
                    .orElse(false);
    }

    /**
     * Specification for high or medium priority
     */
    public static LookupSpecification<TaskPriorityLk> isHighOrMedium() {
        return isHigh().or(isMedium());
    }
}