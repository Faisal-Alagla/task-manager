package com.faisal.taskmanager.utils.validations;

import com.faisal.taskmanager.common.lookups.LookupService;
import com.faisal.taskmanager.common.lookups.LookupType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class LookupValidator implements ConstraintValidator<LookupValidation, Object> {

    private final LookupService lookupService;
    private LookupType lookupType;
    private String errorMessage;
    private boolean isNullable;

    public LookupValidator(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    @Override
    public void initialize(LookupValidation constraintAnnotation) {
        this.lookupType = constraintAnnotation.lookupType();
        this.errorMessage = constraintAnnotation.errorMessage().getMessage();
        this.isNullable = constraintAnnotation.nullable();
    }

    @Override
    public boolean isValid(Object id, ConstraintValidatorContext context) {

        if (id == null) {

            if (isNullable) {
                return true;
            } else {
                addConstraintViolation(context);
                return false;
            }
        }

        boolean isValid = lookupService.findLookupById(lookupType, (Integer) id).isPresent();

        if (!isValid) {
            addConstraintViolation(context);
        }

        return isValid;
    }

    private void addConstraintViolation(ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
    }
}
