package com.faisal.taskmanager.common.exceptions;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Global exception handler for the application.
 *
 * <p>Intercepts exceptions thrown by controllers and converts them into
 * standardized {@link ErrorResponse} objects with appropriate HTTP status codes.</p>
 *
 * @see HandledException
 * @see ErrorResponse
 * @see ErrorMessage
 * @author Faisal
 */
@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    private ErrorResponse createErrorResponse(ErrorMessage message) {
        return ErrorResponse
                .builder()
                .timestamp(new Date())
                .internalCode(message.getInternalCode())
                .message(message.getMessage())
                .build();
    }

    private ErrorResponse createErrorResponse(ErrorMessage message, String description) {
        return ErrorResponse
                .builder()
                .timestamp(new Date())
                .internalCode(message.getInternalCode())
                .message(message.getMessage())
                .description(description)
                .build();
    }

    /**
     * Handles business logic exceptions thrown using {@link HandledException}.
     *
     * <p>Example: {@code .orElseThrow(() -> new HandledException(ErrorMessage.TASK_NOT_FOUND))}</p>
     * <p>If a custom description is provided, it will be used; otherwise, the error message serves as the default description.</p>
     */
    @ExceptionHandler(HandledException.class)
    public ResponseEntity<ErrorResponse> handleHandledException(HandledException ex, WebRequest request) {
        log.error("Handled exception in {}: {}", request.getContextPath(), ex.getErrorMessage().getMessage(), ex);

        // Use custom description if provided, otherwise use the error message as default description
        String description = ex.getDescription() != null
                ? ex.getDescription()
                : ex.getErrorMessage().getMessage();

        ErrorResponse errorResponse = createErrorResponse(ex.getErrorMessage(), description);
        return new ResponseEntity<>(errorResponse, ex.getErrorMessage().getHttpStatus());
    }

    /**
     * Handles bean validation errors from {@code @Valid} or {@code @Validated} annotations.
     *
     * <p>Triggered when request body validation fails.</p>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        log.error("Validation error in {}", request.getContextPath(), ex);

        String validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorResponse errorResponse = createErrorResponse(
                ErrorMessage.INVALID_REQUEST_ATTRIBUTES,
                validationErrors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles constraint violations on method parameters and path variables.
     *
     * <p>Example: {@code @PathVariable @Min(1) Integer id} receives invalid value.</p>
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex,
            WebRequest request) {
        log.error("Constraint violation in {}", request.getContextPath(), ex);

        ErrorResponse response = createErrorResponse(ErrorMessage.CONSTRAINT_VIOLATED_ERROR);

        ex.getConstraintViolations().stream()
                .findFirst()
                .ifPresent(violation -> response.setDescription(violation.getMessage()));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles type conversion errors for method arguments.
     *
     * <p>Example: {@code /api/tasks/abc} when UUID is expected.</p>
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            WebRequest request) {
        log.error("Argument type mismatch in {}", request.getContextPath(), ex);

        String description = String.format("Parameter '%s' should be of type %s",
                ex.getPropertyName(),
                Objects.requireNonNull(ex.getRequiredType()).getSimpleName());

        ErrorResponse response = createErrorResponse(
                ErrorMessage.METHOD_ARGUMENT_MISMATCH_ERROR,
                description);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles errors when HTTP message body cannot be read or parsed.
     *
     * <p>Triggered by malformed JSON or invalid request body format.</p>
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            WebRequest request) {
        log.error("HTTP message not readable in {}", request.getContextPath(), ex);

        ErrorResponse response = createErrorResponse(ErrorMessage.HTTP_MESSAGE_NOT_READABLE_ERROR);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles requests to non-existent resource paths.
     *
     * <p>Example: {@code GET /api/nonexistent}</p>
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(
            NoResourceFoundException ex,
            WebRequest request) {
        log.error("Resource not found: {}", request.getContextPath(), ex);

        ErrorResponse response = createErrorResponse(
                ErrorMessage.RESOURCE_NOT_FOUND_ERROR,
                ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles database constraint violations.
     *
     * <p>Triggered by unique key, foreign key, not-null, or check constraint violations.</p>
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            WebRequest request) {
        log.error("Data integrity violation in {}", request.getContextPath(), ex);

        ErrorResponse response = createErrorResponse(ErrorMessage.DATA_INTEGRITY_VIOLATION_ERROR);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /**
     * Global fallback handler for any unhandled exceptions.
     *
     * <p>Returns a generic 500 error without exposing internal details.</p>
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        log.error("Unhandled exception in {}", request.getContextPath(), ex);

        ErrorResponse errorResponse = createErrorResponse(ErrorMessage.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}