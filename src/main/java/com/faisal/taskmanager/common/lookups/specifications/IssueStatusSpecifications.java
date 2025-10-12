package com.faisal.taskmanager.common.lookups.specifications;

import com.faisal.taskmanager.common.lookups.entities.IssueStatusLk;
import com.faisal.taskmanager.common.lookups.enums.IssueStatusEnum;

public class IssueStatusSpecifications {

    /**
     * Specification for resolved issues
     */
    public static LookupSpecification<IssueStatusLk> isResolved() {
        return () -> status -> IssueStatusEnum.fromId(status.getId())
                    .map(IssueStatusEnum::isResolved)
                    .orElse(false);
    }

    /**
     * Specification for open/in-progress issues
     */
    public static LookupSpecification<IssueStatusLk> isOpen() {
        return () -> status -> IssueStatusEnum.fromId(status.getId())
                    .map(IssueStatusEnum::isOpen)
                    .orElse(false);
    }

    /**
     * Specification for unresolved issues
     */
    public static LookupSpecification<IssueStatusLk> isUnresolved() {
        return isResolved().not();
    }
}