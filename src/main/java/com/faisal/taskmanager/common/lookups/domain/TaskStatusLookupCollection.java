package com.faisal.taskmanager.common.lookups.domain;

import com.faisal.taskmanager.common.lookups.entities.TaskStatusLk;
import com.faisal.taskmanager.common.lookups.enums.TaskStatusEnum;
import com.faisal.taskmanager.common.lookups.specifications.TaskStatusSpecifications;

import java.util.List;

public class TaskStatusLookupCollection extends BaseLookupCollection<TaskStatusLk> {

    public TaskStatusLookupCollection(List<TaskStatusLk> items) {
        super(items);
    }

    /**
     * Convert entity ID to enum
     */
    public TaskStatusEnum toEnum(Integer statusId) {
        return TaskStatusEnum.fromId(statusId).orElse(null);
    }

    /**
     * Get entity by enum
     */
    public TaskStatusLk getByEnum(TaskStatusEnum statusEnum) {
        return findById(statusEnum.getId());
    }

    /**
     * Get all terminal statuses (completed/cancelled)
     */
    public List<TaskStatusLk> getTerminalStatuses() {
        return findBy(TaskStatusSpecifications.isTerminal());
    }

    /**
     * Get all active statuses (in progress)
     */
    public List<TaskStatusLk> getActiveStatuses() {
        return findBy(TaskStatusSpecifications.isActive());
    }

    /**
     * Get all blocked statuses (on hold)
     */
    public List<TaskStatusLk> getBlockedStatuses() {
        return findBy(TaskStatusSpecifications.isBlocked());
    }

    /**
     * Get all open statuses (not terminal)
     */
    public List<TaskStatusLk> getOpenStatuses() {
        return findBy(TaskStatusSpecifications.isOpen());
    }

    /**
     * Check if status is terminal (completed/cancelled)
     */
    public boolean isTerminal(Integer statusId) {
        return TaskStatusEnum.fromId(statusId)
                .map(TaskStatusEnum::isTerminal)
                .orElse(false);
    }

    /**
     * Check if status is completed
     */
    public boolean isCompleted(Integer statusId) {
        return TaskStatusEnum.fromId(statusId)
                .map(e -> e == TaskStatusEnum.COMPLETED)
                .orElse(false);
    }

    /**
     * Check if status is cancelled
     */
    public boolean isCancelled(Integer statusId) {
        return TaskStatusEnum.fromId(statusId)
                .map(e -> e == TaskStatusEnum.CANCELLED)
                .orElse(false);
    }

    /**
     * Check if status is active (in progress)
     */
    public boolean isActive(Integer statusId) {
        return TaskStatusEnum.fromId(statusId)
                .map(TaskStatusEnum::isActive)
                .orElse(false);
    }

    /**
     * Check if status is blocked (on hold)
     */
    public boolean isBlocked(Integer statusId) {
        return TaskStatusEnum.fromId(statusId)
                .map(TaskStatusEnum::isBlocked)
                .orElse(false);
    }
}