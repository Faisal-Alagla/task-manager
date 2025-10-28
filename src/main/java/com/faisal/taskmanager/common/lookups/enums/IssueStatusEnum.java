package com.faisal.taskmanager.common.lookups.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IssueStatusEnum {
    IN_PROGRESS(0, "in_progress"),
    RESOLVED(1, "resolved");

    private final Integer id;
    private final String name;
}
