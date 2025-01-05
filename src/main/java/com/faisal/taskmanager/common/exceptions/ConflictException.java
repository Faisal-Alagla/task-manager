package com.faisal.taskmanager.common.exceptions;

import lombok.Getter;

@Getter
public class ConflictException extends RuntimeException {
    private final ErrorMessage errorMessage;

    public ConflictException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }
}
