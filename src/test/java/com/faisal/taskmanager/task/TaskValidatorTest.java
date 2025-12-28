package com.faisal.taskmanager.task;

import com.faisal.taskmanager.common.exceptions.ErrorMessage;
import com.faisal.taskmanager.common.exceptions.HandledException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.faisal.taskmanager.testutils.builders.TaskTestBuilder.aTask;
import static com.faisal.taskmanager.testutils.constants.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("TaskValidator")
class TaskValidatorTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskValidator taskValidator;

    // ========== validateTaskExists() tests ==========

    @Test
    @DisplayName("validateTaskExists - should not throw when task exists")
    void validateTaskExists_shouldNotThrowWhenTaskExists() {
        Task existingTask = aTask().withId(TASK_ID_1).build();

        when(taskRepository.findByIdAndIsActiveTrue(TASK_ID_1)).thenReturn(Optional.of(existingTask));

        assertThatCode(() -> taskValidator.validateTaskExists(TASK_ID_1, "taskId"))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("validateTaskExists - should call repository to check existence")
    void validateTaskExists_shouldCallRepositoryToCheckExistence() {
        Task existingTask = aTask().withId(TASK_ID_1).build();

        when(taskRepository.findByIdAndIsActiveTrue(TASK_ID_1)).thenReturn(Optional.of(existingTask));

        taskValidator.validateTaskExists(TASK_ID_1, "taskId");

        verify(taskRepository).findByIdAndIsActiveTrue(TASK_ID_1);
    }

    @Test
    @DisplayName("validateTaskExists - should throw when task does not exist")
    void validateTaskExists_shouldThrowWhenTaskDoesNotExist() {
        when(taskRepository.findByIdAndIsActiveTrue(NON_EXISTENT_TASK_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskValidator.validateTaskExists(NON_EXISTENT_TASK_ID, "taskId"))
                .isInstanceOf(HandledException.class)
                .satisfies(e -> {
                    HandledException he = (HandledException) e;
                    assertThat(he.getErrorDetails()).hasSize(1);
                    assertThat(he.getErrorDetails().get(0).getInternalCode())
                            .isEqualTo(ErrorMessage.TASK_NOT_FOUND.getInternalCode());
                    assertThat(he.getErrorDetails().get(0).getField()).isEqualTo("taskId");
                });
    }

    @Test
    @DisplayName("validateTaskExists - should include correct field name in error")
    void validateTaskExists_shouldIncludeCorrectFieldNameInError() {
        when(taskRepository.findByIdAndIsActiveTrue(NON_EXISTENT_TASK_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskValidator.validateTaskExists(NON_EXISTENT_TASK_ID, "parentTaskId"))
                .isInstanceOf(HandledException.class)
                .satisfies(e -> {
                    HandledException he = (HandledException) e;
                    assertThat(he.getErrorDetails().get(0).getField()).isEqualTo("parentTaskId");
                });
    }

    @Test
    @DisplayName("validateTaskExists - should not throw when taskId is null")
    void validateTaskExists_shouldNotThrowWhenTaskIdIsNull() {
        assertThatCode(() -> taskValidator.validateTaskExists(null, "taskId"))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("validateTaskExists - should not call repository when taskId is null")
    void validateTaskExists_shouldNotCallRepositoryWhenTaskIdIsNull() {
        taskValidator.validateTaskExists(null, "taskId");

        verify(taskRepository, never()).findByIdAndIsActiveTrue(any());
    }

    // ========== validateParentTaskExists() tests ==========

    @Test
    @DisplayName("validateParentTaskExists - should not throw when parent task exists")
    void validateParentTaskExists_shouldNotThrowWhenParentTaskExists() {
        Task existingTask = aTask().withId(PARENT_TASK_ID).build();

        when(taskRepository.findByIdAndIsActiveTrue(PARENT_TASK_ID)).thenReturn(Optional.of(existingTask));

        assertThatCode(() -> taskValidator.validateParentTaskExists(PARENT_TASK_ID))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("validateParentTaskExists - should call validateTaskExists with correct field name")
    void validateParentTaskExists_shouldCallValidateTaskExistsWithCorrectFieldName() {
        Task existingTask = aTask().withId(PARENT_TASK_ID).build();

        when(taskRepository.findByIdAndIsActiveTrue(PARENT_TASK_ID)).thenReturn(Optional.of(existingTask));

        taskValidator.validateParentTaskExists(PARENT_TASK_ID);

        verify(taskRepository).findByIdAndIsActiveTrue(PARENT_TASK_ID);
    }

    @Test
    @DisplayName("validateParentTaskExists - should throw with parentTaskId field when task does not exist")
    void validateParentTaskExists_shouldThrowWithParentTaskIdFieldWhenTaskDoesNotExist() {
        when(taskRepository.findByIdAndIsActiveTrue(NON_EXISTENT_TASK_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskValidator.validateParentTaskExists(NON_EXISTENT_TASK_ID))
                .isInstanceOf(HandledException.class)
                .satisfies(e -> {
                    HandledException he = (HandledException) e;
                    assertThat(he.getErrorDetails().get(0).getField()).isEqualTo("parentTaskId");
                });
    }

    @Test
    @DisplayName("validateParentTaskExists - should not throw when parentTaskId is null")
    void validateParentTaskExists_shouldNotThrowWhenParentTaskIdIsNull() {
        assertThatCode(() -> taskValidator.validateParentTaskExists(null))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("validateParentTaskExists - should not call repository when parentTaskId is null")
    void validateParentTaskExists_shouldNotCallRepositoryWhenParentTaskIdIsNull() {
        taskValidator.validateParentTaskExists(null);

        verify(taskRepository, never()).findByIdAndIsActiveTrue(any());
    }
}
