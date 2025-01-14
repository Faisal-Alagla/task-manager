package com.faisal.taskmanager.activity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ActivityRequestDto {

    private String httpMethod;

    private String uri;

    private String operation;

    private String operationReferenceId;

    private String message;

    private UUID userId;
}
