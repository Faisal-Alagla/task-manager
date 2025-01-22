package com.faisal.taskmanager.utils.validations;

import com.faisal.taskmanager.common.exceptions.ErrorMessage;
import com.faisal.taskmanager.task.ITaskService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TaskExistsValidator implements ConstraintValidator<TaskExistsValidation, UUID> {

    private final ITaskService iTaskService;

    private boolean nullable;

    @Override
    public void initialize(TaskExistsValidation constraintAnnotation) {
        this.nullable = constraintAnnotation.nullable();
    }

    @Override
    public boolean isValid(UUID taskId, ConstraintValidatorContext constraintValidatorContext) {

        if (taskId == null) return nullable;

        if (!iTaskService.taskExists(taskId)) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(ErrorMessage.TASK_NOT_FOUND.getMessage())
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
