package com.faisal.taskmanager.common.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("ControllerExceptionHandler Tests")
class ControllerExceptionHandlerTest {

    private ControllerExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new ControllerExceptionHandler();
    }

    // Helper method to create a MethodParameter for testing
    private MethodParameter createMethodParameter() throws NoSuchMethodException {
        return new MethodParameter(
            ControllerExceptionHandlerTest.class.getDeclaredMethod("setUp"),
            -1
        );
    }

    @Nested
    @DisplayName("HandledException Tests")
    class HandledExceptionTests {

        @Test
        @DisplayName("Should handle HandledException and return correct error response")
        void handleHandledException_ShouldReturnCorrectErrorResponse() {
            // Arrange
            HandledException exception = new HandledException(ErrorMessage.TASK_NOT_FOUND);
            WebRequest request = mock(WebRequest.class);

            // Act
            ResponseEntity<ErrorResponse> response = exceptionHandler.handleHandledException(exception, request);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(ErrorMessage.TASK_NOT_FOUND.getHttpStatus());
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getErrors()).isNotNull().hasSize(1);
            assertThat(response.getBody().getErrors().getFirst().getInternalCode()).isEqualTo(ErrorMessage.TASK_NOT_FOUND.getInternalCode());
            assertThat(response.getBody().getErrors().getFirst().getMessage()).isEqualTo(ErrorMessage.TASK_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("Should handle different error messages correctly")
        void handleHandledException_WithDifferentMessages_ShouldReturnCorrectResponse() {
            // Arrange
            HandledException exception = new HandledException(ErrorMessage.INVALID_STATUS_TRANSITION);
            WebRequest request = mock(WebRequest.class);

            // Act
            ResponseEntity<ErrorResponse> response = exceptionHandler.handleHandledException(exception, request);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(ErrorMessage.INVALID_STATUS_TRANSITION.getHttpStatus());
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getErrors()).isNotNull().hasSize(1);
            assertThat(response.getBody().getErrors().getFirst().getInternalCode()).isEqualTo(ErrorMessage.INVALID_STATUS_TRANSITION.getInternalCode());
        }
    }

    @Nested
    @DisplayName("MethodArgumentNotValid Tests")
    class MethodArgumentNotValidTests {

        @Test
        @DisplayName("Should handle validation errors and return formatted response")
        void handleMethodArgumentNotValid_ShouldReturnFormattedErrors() throws NoSuchMethodException {
            // Arrange
            BindingResult bindingResult = mock(BindingResult.class);
            FieldError fieldError1 = new FieldError("taskDto", "name", "Name cannot be blank");
            FieldError fieldError2 = new FieldError("taskDto", "description", "Description is too long");
            when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

            MethodArgumentNotValidException exception = new MethodArgumentNotValidException(
                    createMethodParameter(), bindingResult);
            WebRequest request = mock(WebRequest.class);

            // Act
            ResponseEntity<ErrorResponse> response = exceptionHandler.handleMethodArgumentNotValid(exception, request);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getErrors()).isNotNull().hasSize(2);
            assertThat(response.getBody().getErrors().get(0).getField()).isEqualTo("name");
            assertThat(response.getBody().getErrors().get(0).getDescription()).isEqualTo("Name cannot be blank");
            assertThat(response.getBody().getErrors().get(1).getField()).isEqualTo("description");
            assertThat(response.getBody().getErrors().get(1).getDescription()).isEqualTo("Description is too long");
        }
    }

    @Nested
    @DisplayName("ConstraintViolation Tests")
    class ConstraintViolationTests {

        @Test
        @DisplayName("Should handle constraint violations and return formatted response")
        void handleConstraintViolation_ShouldReturnFormattedErrors() {
            // Arrange
            Set<ConstraintViolation<?>> violations = new HashSet<>();
            ConstraintViolation<?> violation = mock(ConstraintViolation.class);

            when(violation.getPropertyPath()).thenReturn(mock(jakarta.validation.Path.class));
            when(violation.getPropertyPath().toString()).thenReturn("taskId");
            when(violation.getMessage()).thenReturn("Invalid task ID");

            violations.add(violation);

            ConstraintViolationException exception = new ConstraintViolationException(violations);
            WebRequest request = mock(WebRequest.class);

            // Act
            ResponseEntity<ErrorResponse> response = exceptionHandler.handleConstraintViolation(exception, request);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getErrors()).isNotNull().hasSize(1);
            assertThat(response.getBody().getErrors().getFirst().getDescription()).isEqualTo("Invalid task ID");
        }
    }

    @Nested
    @DisplayName("MethodArgumentTypeMismatch Tests")
    class MethodArgumentTypeMismatchTests {

        @Test
        @DisplayName("Should handle type mismatch errors")
        void handleMethodArgumentTypeMismatch_ShouldReturnBadRequest() {
            // Arrange
            MethodArgumentTypeMismatchException exception = new MethodArgumentTypeMismatchException(
                    "invalid-uuid", String.class, "taskId", null, null);
            WebRequest request = mock(WebRequest.class);

            // Act
            ResponseEntity<ErrorResponse> response = exceptionHandler.handleMethodArgumentTypeMismatch(exception, request);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getErrors()).isNotNull().hasSize(1);
            assertThat(response.getBody().getErrors().getFirst().getDescription()).contains("taskId");
        }
    }

    @Nested
    @DisplayName("HttpMessageNotReadable Tests")
    class HttpMessageNotReadableTests {

        @Test
        @DisplayName("Should handle malformed JSON errors")
        void handleHttpMessageNotReadable_ShouldReturnBadRequest() {
            // Arrange
            HttpMessageNotReadableException exception = mock(HttpMessageNotReadableException.class);
            WebRequest request = mock(WebRequest.class);

            // Act
            ResponseEntity<ErrorResponse> response = exceptionHandler.handleHttpMessageNotReadable(exception, request);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).isNotNull();
        }
    }

    @Nested
    @DisplayName("DataIntegrityViolation Tests")
    class DataIntegrityViolationTests {

        @Test
        @DisplayName("Should handle database constraint violations")
        void handleDataIntegrityViolation_ShouldReturnConflict() {
            // Arrange
            DataIntegrityViolationException exception = new DataIntegrityViolationException("Unique constraint violated");
            WebRequest request = mock(WebRequest.class);

            // Act
            ResponseEntity<ErrorResponse> response = exceptionHandler.handleDataIntegrityViolation(exception, request);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            assertThat(response.getBody()).isNotNull();
        }
    }

    @Nested
    @DisplayName("Global Exception Tests")
    class GlobalExceptionTests {

        @Test
        @DisplayName("Should handle unexpected exceptions")
        void handleGlobalException_ShouldReturnInternalServerError() {
            // Arrange
            Exception exception = new RuntimeException("Unexpected error");
            WebRequest request = mock(WebRequest.class);

            // Act
            ResponseEntity<ErrorResponse> response = exceptionHandler.handleGlobalException(exception, request);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            assertThat(response.getBody()).isNotNull();
        }
    }
}
