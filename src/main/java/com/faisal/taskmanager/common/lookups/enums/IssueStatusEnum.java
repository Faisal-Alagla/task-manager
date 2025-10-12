package com.faisal.taskmanager.common.lookups.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum IssueStatusEnum {
    IN_PROGRESS(0, "in_progress"),
    RESOLVED(1, "resolved");

    private final Integer id;
    private final String name;

    /**
     * Find enum by database ID
     */
    public static Optional<IssueStatusEnum> fromId(Integer id) {
        return Arrays.stream(values())
                .filter(status -> status.getId().equals(id))
                .findFirst();
    }

    /**
     * Find enum by database name
     */
    public static Optional<IssueStatusEnum> fromName(String name) {
        return Arrays.stream(values())
                .filter(status -> status.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    /**
     * Check if issue is resolved
     */
    public boolean isResolved() {
        return this == RESOLVED;
    }

    /**
     * Check if issue is still open
     */
    public boolean isOpen() {
        return this == IN_PROGRESS;
    }
}