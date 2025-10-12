package com.faisal.taskmanager.common.lookups.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum IssueCriticalityEnum {
    HIGH(0, "high"),
    MEDIUM(1, "medium"),
    LOW(2, "low");

    private final Integer id;
    private final String name;

    public static Optional<IssueCriticalityEnum> fromId(Integer id) {
        return Arrays.stream(values())
                .filter(criticality -> criticality.getId().equals(id))
                .findFirst();
    }

    public static Optional<IssueCriticalityEnum> fromName(String name) {
        return Arrays.stream(values())
                .filter(criticality -> criticality.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public boolean isHigh() {
        return this == HIGH;
    }

    public boolean isLow() {
        return this == LOW;
    }

    public int getSeverityLevel() {
        return switch (this) {
            case HIGH -> 3;
            case MEDIUM -> 2;
            case LOW -> 1;
        };
    }

    public boolean requiresImmediateAttention() {
        return this == HIGH;
    }
}