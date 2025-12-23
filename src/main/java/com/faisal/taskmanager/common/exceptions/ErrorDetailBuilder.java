package com.faisal.taskmanager.common.exceptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Fluent builder for accumulating multiple error details.
 *
 * <p>Provides a clean, declarative API for building field-level and general errors.
 * Particularly useful for validation scenarios where multiple errors need to be collected.</p>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * new ErrorDetailBuilder()
 *     .addIf(nameInvalid, ErrorMessage.TASK_NAME_REQUIRED, "name")
 *     .addIf(dueDateInvalid, ErrorMessage.TASK_DUE_DATE_INVALID, "dueDate")
 *     .throwIfHasErrors();
 * }</pre>
 *
 * @see ErrorDetail
 * @see HandledException
 * @author Faisal
 */
public class ErrorDetailBuilder {

    private final List<ErrorDetail> errors = new ArrayList<>();

    /**
     * Adds a field-level error if the condition is true.
     *
     * @param condition the condition to check
     * @param errorMessage the error message enum
     * @param field the field name associated with the error
     * @return this builder for method chaining
     */
    public ErrorDetailBuilder addIf(boolean condition, ErrorMessage errorMessage, String field) {
        return addIf(condition, errorMessage, field, null);
    }

    /**
     * Adds a field-level error with custom description if the condition is true.
     *
     * @param condition the condition to check
     * @param errorMessage the error message enum
     * @param field the field name associated with the error
     * @param description additional context or description (can be null)
     * @return this builder for method chaining
     */
    public ErrorDetailBuilder addIf(boolean condition, ErrorMessage errorMessage, String field, String description) {
        if (condition) {
            errors.add(ErrorDetail.builder()
                    .internalCode(errorMessage.getInternalCode())
                    .message(errorMessage.getMessage())
                    .field(field)
                    .description(description)
                    .build());
        }
        return this;
    }

    /**
     * Adds a general error with description (no field) if the condition is true.
     *
     * @param condition the condition to check
     * @param errorMessage the error message enum
     * @param description additional context or description
     * @return this builder for method chaining
     */
    public ErrorDetailBuilder addIfWithDescription(boolean condition, ErrorMessage errorMessage, String description) {
        return addIf(condition, errorMessage, null, description);
    }

    /**
     * Adds a field-level error unconditionally.
     *
     * @param errorMessage the error message enum
     * @param field the field name associated with the error
     * @return this builder for method chaining
     */
    public ErrorDetailBuilder add(ErrorMessage errorMessage, String field) {
        return addIf(true, errorMessage, field);
    }

    /**
     * Adds a field-level error with custom description unconditionally.
     *
     * @param errorMessage the error message enum
     * @param field the field name associated with the error
     * @param description additional context or description
     * @return this builder for method chaining
     */
    public ErrorDetailBuilder add(ErrorMessage errorMessage, String field, String description) {
        return addIf(true, errorMessage, field, description);
    }

    /**
     * Throws a HandledException if any errors have been accumulated.
     *
     * @throws HandledException if errors exist
     */
    public void throwIfHasErrors() {
        if (!errors.isEmpty()) {
            throw new HandledException(errors);
        }
    }

    /**
     * Checks if any errors have been accumulated.
     *
     * @return true if errors exist, false otherwise
     */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    /**
     * Returns a copy of the accumulated errors.
     *
     * @return list of error details
     */
    public List<ErrorDetail> getErrors() {
        return new ArrayList<>(errors);
    }
}
