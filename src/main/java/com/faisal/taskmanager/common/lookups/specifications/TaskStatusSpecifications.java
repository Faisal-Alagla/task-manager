package com.faisal.taskmanager.common.lookups.specifications;

import com.faisal.taskmanager.common.lookups.entities.TaskStatusLk;
import com.faisal.taskmanager.common.lookups.enums.TaskStatusEnum;

public class TaskStatusSpecifications {

    /**
     * Specification for completed/cancelled statuses
     */
    public static LookupSpecification<TaskStatusLk> isTerminal() {
        return () -> status -> TaskStatusEnum.fromId(status.getId())
                    .map(TaskStatusEnum::isTerminal)
                    .orElse(false);
    }

    /**
     * Specification for in-progress statuses
     */
    public static LookupSpecification<TaskStatusLk> isActive() {
        return () -> status -> TaskStatusEnum.fromId(status.getId())
                    .map(TaskStatusEnum::isActive)
                    .orElse(false);
    }

    /**
     * Specification for blocked/on-hold statuses
     */
    public static LookupSpecification<TaskStatusLk> isBlocked() {
        return () -> status -> TaskStatusEnum.fromId(status.getId())
                    .map(TaskStatusEnum::isBlocked)
                    .orElse(false);
    }

    /**
     * Specification for completed status specifically
     */
    public static LookupSpecification<TaskStatusLk> isCompleted() {
        return () -> status -> TaskStatusEnum.fromId(status.getId())
                    .map(e -> e == TaskStatusEnum.COMPLETED)
                    .orElse(false);
    }

    /**
     * Specification for cancelled status
     */
    public static LookupSpecification<TaskStatusLk> isCancelled() {
        return () -> status -> TaskStatusEnum.fromId(status.getId())
                    .map(e -> e == TaskStatusEnum.CANCELLED)
                    .orElse(false);
    }

    /**
     * Specification for open statuses (not terminal)
     */
    public static LookupSpecification<TaskStatusLk> isOpen() {
        return isTerminal().not();
    }
}