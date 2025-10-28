package com.faisal.taskmanager.common.lookups.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskStatusEnum {
    IN_PROGRESS(0, "in_progress"),
    COMPLETED(1, "completed"),
    CANCELLED(2, "cancelled"),
    ON_HOLD(3, "on_hold");

    private final Integer id;
    private final String name;
}
