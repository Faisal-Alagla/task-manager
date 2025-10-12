package com.faisal.taskmanager.common.lookups.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum TaskPriorityEnum {
    HIGH(0, "high"),
    MEDIUM(1, "medium"),
    LOW(2, "low");

    private final Integer id;
    private final String name;

    /**
     * Find enum by database ID
     */
    public static Optional<TaskPriorityEnum> fromId(Integer id) {
        return Arrays.stream(values())
                .filter(priority -> priority.getId().equals(id))
                .findFirst();
    }

    /**
     * Find enum by database name
     */
    public static Optional<TaskPriorityEnum> fromName(String name) {
        return Arrays.stream(values())
                .filter(priority -> priority.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    /**
     * Check if this is a high priority
     */
    public boolean isHigh() {
        return this == HIGH;
    }

    /**
     * Check if this is low priority
     */
    public boolean isLow() {
        return this == LOW;
    }

    /**
     * Get priority level (higher number = higher priority)
     */
    public int getPriorityLevel() {
        return switch (this) {
            case HIGH -> 3;
            case MEDIUM -> 2;
            case LOW -> 1;
        };
    }

    /**
     * Check if this priority is higher than another
     */
    public boolean isHigherThan(TaskPriorityEnum other) {
        return this.getPriorityLevel() > other.getPriorityLevel();
    }
}