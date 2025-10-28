package com.faisal.taskmanager.common.lookups.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskPriorityEnum {
    HIGH(0, "high"),
    MEDIUM(1, "medium"),
    LOW(2, "low");

    private final Integer id;
    private final String name;
}
