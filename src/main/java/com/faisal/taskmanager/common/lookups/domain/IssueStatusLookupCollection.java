package com.faisal.taskmanager.common.lookups.domain;

import com.faisal.taskmanager.common.lookups.entities.IssueStatusLk;
import com.faisal.taskmanager.common.lookups.enums.IssueStatusEnum;
import com.faisal.taskmanager.common.lookups.specifications.IssueStatusSpecifications;

import java.util.List;

public class IssueStatusLookupCollection extends BaseLookupCollection<IssueStatusLk> {

    public IssueStatusLookupCollection(List<IssueStatusLk> items) {
        super(items);
    }

    /**
     * Convert entity ID to enum
     */
    public IssueStatusEnum toEnum(Integer statusId) {
        return IssueStatusEnum.fromId(statusId).orElse(null);
    }

    /**
     * Get entity by enum
     */
    public IssueStatusLk getByEnum(IssueStatusEnum statusEnum) {
        return findById(statusEnum.getId());
    }

    /**
     * Get resolved statuses
     */
    public List<IssueStatusLk> getResolvedStatuses() {
        return findBy(IssueStatusSpecifications.isResolved());
    }

    /**
     * Get open statuses
     */
    public List<IssueStatusLk> getOpenStatuses() {
        return findBy(IssueStatusSpecifications.isOpen());
    }

    /**
     * Check if status is resolved
     */
    public boolean isResolved(Integer statusId) {
        return IssueStatusEnum.fromId(statusId)
                .map(IssueStatusEnum::isResolved)
                .orElse(false);
    }

    /**
     * Check if status is open
     */
    public boolean isOpen(Integer statusId) {
        return IssueStatusEnum.fromId(statusId)
                .map(IssueStatusEnum::isOpen)
                .orElse(false);
    }
}