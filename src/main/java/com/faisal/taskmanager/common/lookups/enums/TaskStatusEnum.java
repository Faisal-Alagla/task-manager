package com.faisal.taskmanager.common.lookups.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum TaskStatusEnum {
    IN_PROGRESS(0, "in_progress"),
    COMPLETED(1, "completed"),
    CANCELLED(2, "cancelled"),
    ON_HOLD(3, "on_hold");

    private final Integer id;
    private final String name;

    /**
     * Find enum by database ID
     */
    public static Optional<TaskStatusEnum> fromId(Integer id) {
        return Arrays.stream(values())
                .filter(status -> status.getId().equals(id))
                .findFirst();
    }

    /**
     * Find enum by database name
     */
    public static Optional<TaskStatusEnum> fromName(String name) {
        return Arrays.stream(values())
                .filter(status -> status.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    /**
     * Check if this status represents completion
     */
    public boolean isTerminal() {
        return this == COMPLETED || this == CANCELLED;
    }

    /**
     * Check if this status is active (can be worked on)
     */
    public boolean isActive() {
        return this == IN_PROGRESS;
    }

    /**
     * Check if this status is blocked
     */
    public boolean isBlocked() {
        return this == ON_HOLD;
    }
}