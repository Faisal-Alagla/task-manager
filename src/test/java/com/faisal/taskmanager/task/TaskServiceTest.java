package com.faisal.taskmanager.task;

import com.faisal.taskmanager.common.exceptions.ErrorMessage;
import com.faisal.taskmanager.common.exceptions.HandledException;
import com.faisal.taskmanager.common.lookups.LookupService;
import com.faisal.taskmanager.issue.IIssueService;
import com.faisal.taskmanager.task.dto.TaskCreationDto;
import com.faisal.taskmanager.task.dto.TaskResponseDto;
import com.faisal.taskmanager.task.dto.TaskUpdateDto;
import jakarta.persistence.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static com.faisal.taskmanager.testutils.builders.TaskCreationDtoBuilder.aTaskCreationDto;
import static com.faisal.taskmanager.testutils.builders.TaskTestBuilder.aTask;
import static com.faisal.taskmanager.testutils.builders.TaskUpdateDtoBuilder.aTaskUpdateDto;
import static com.faisal.taskmanager.testutils.constants.TestConstants.*;
import static com.faisal.taskmanager.testutils.fixtures.MockLookupFactory.createTaskPriorityCollection;
import static com.faisal.taskmanager.testutils.fixtures.MockLookupFactory.createTaskStatusCollection;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TaskService")
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private LookupService lookupService;

    @Mock
    private IIssueService issueService;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        when(lookupService.getTaskStatusCollection()).thenReturn(createTaskStatusCollection());
        when(lookupService.getTaskPriorityCollection()).thenReturn(createTaskPriorityCollection());

        taskService = new TaskService(taskRepository, lookupService, issueService);
        ReflectionTestUtils.invokeMethod(taskService, "init");
    }

    // ========== createTask() tests ==========

    @Test
    @DisplayName("createTask - should validate status ID")
    void createTask_shouldValidateStatusId() {
        TaskCreationDto dto = aTaskCreationDto()
                .withStatusId(INVALID_TASK_STATUS_ID)
                .build();

        assertThatThrownBy(() -> taskService.createTask(dto))
                .isInstanceOf(HandledException.class)
                .hasFieldOrPropertyWithValue("errorMessage", ErrorMessage.TASK_STATUS_NOT_FOUND);
    }

    @Test
    @DisplayName("createTask - should not call repository when status invalid")
    void createTask_shouldNotCallRepositoryWhenStatusInvalid() {
        TaskCreationDto dto = aTaskCreationDto()
                .withStatusId(INVALID_TASK_STATUS_ID)
                .build();

        try {
            taskService.createTask(dto);
        } catch (HandledException e) {
            // Expected
        }

        verify(taskRepository, never()).saveWithClosure(any(), any());
    }

    @Test
    @DisplayName("createTask - should validate priority ID")
    void createTask_shouldValidatePriorityId() {
        TaskCreationDto dto = aTaskCreationDto()
                .withPriorityId(INVALID_TASK_PRIORITY_ID)
                .build();

        assertThatThrownBy(() -> taskService.createTask(dto))
                .isInstanceOf(HandledException.class)
                .hasFieldOrPropertyWithValue("errorMessage", ErrorMessage.TASK_PRIORITY_NOT_FOUND);
    }

    @Test
    @DisplayName("createTask - should not call repository when priority invalid")
    void createTask_shouldNotCallRepositoryWhenPriorityInvalid() {
        TaskCreationDto dto = aTaskCreationDto()
                .withPriorityId(INVALID_TASK_PRIORITY_ID)
                .build();

        try {
            taskService.createTask(dto);
        } catch (HandledException e) {
            // Expected
        }

        verify(taskRepository, never()).saveWithClosure(any(), any());
    }

    @Test
    @DisplayName("createTask - should call saveWithClosure")
    void createTask_shouldCallSaveWithClosure() {
        TaskCreationDto dto = aTaskCreationDto().build();
        Task savedTask = aTask().withId(TASK_ID_1).build();
        Tuple mockTuple = mock(Tuple.class);

        when(taskRepository.saveWithClosure(any(Task.class), any())).thenReturn(savedTask);
        when(taskRepository.findTaskByIdWithRelations(TASK_ID_1)).thenReturn(Optional.of(mockTuple));

        taskService.createTask(dto);

        verify(taskRepository).saveWithClosure(any(Task.class), any());
    }

    @Test
    @DisplayName("createTask - should pass null parent when no parent ID")
    void createTask_shouldPassNullParentWhenNoParentId() {
        TaskCreationDto dto = aTaskCreationDto().withParentTaskId(null).build();
        Task savedTask = aTask().build();
        Tuple mockTuple = mock(Tuple.class);

        when(taskRepository.saveWithClosure(any(Task.class), eq(null))).thenReturn(savedTask);
        when(taskRepository.findTaskByIdWithRelations(any())).thenReturn(Optional.of(mockTuple));

        taskService.createTask(dto);

        verify(taskRepository).saveWithClosure(any(Task.class), eq(null));
    }

    @Test
    @DisplayName("createTask - should pass parent ID when provided")
    void createTask_shouldPassParentIdWhenProvided() {
        TaskCreationDto dto = aTaskCreationDto().withParentTaskId(PARENT_TASK_ID).build();
        Task savedTask = aTask().build();
        Tuple mockTuple = mock(Tuple.class);

        when(taskRepository.saveWithClosure(any(Task.class), eq(PARENT_TASK_ID))).thenReturn(savedTask);
        when(taskRepository.findTaskByIdWithRelations(any())).thenReturn(Optional.of(mockTuple));

        taskService.createTask(dto);

        verify(taskRepository).saveWithClosure(any(Task.class), eq(PARENT_TASK_ID));
    }

    @Test
    @DisplayName("createTask - should call findTaskByIdWithRelations")
    void createTask_shouldCallFindTaskByIdWithRelations() {
        TaskCreationDto dto = aTaskCreationDto().build();
        Task savedTask = aTask().withId(TASK_ID_1).build();
        Tuple mockTuple = mock(Tuple.class);

        when(taskRepository.saveWithClosure(any(Task.class), any())).thenReturn(savedTask);
        when(taskRepository.findTaskByIdWithRelations(TASK_ID_1)).thenReturn(Optional.of(mockTuple));

        taskService.createTask(dto);

        verify(taskRepository).findTaskByIdWithRelations(TASK_ID_1);
    }

    @Test
    @DisplayName("createTask - should throw when task not found after save")
    void createTask_shouldThrowWhenTaskNotFoundAfterSave() {
        TaskCreationDto dto = aTaskCreationDto().build();
        Task savedTask = aTask().withId(TASK_ID_1).build();

        when(taskRepository.saveWithClosure(any(Task.class), any())).thenReturn(savedTask);
        when(taskRepository.findTaskByIdWithRelations(TASK_ID_1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.createTask(dto))
                .isInstanceOf(HandledException.class)
                .hasFieldOrPropertyWithValue("errorMessage", ErrorMessage.TASK_NOT_FOUND);
    }

    @Test
    @DisplayName("createTask - should return task response DTO")
    void createTask_shouldReturnTaskResponseDto() {
        TaskCreationDto dto = aTaskCreationDto().build();
        Task savedTask = aTask().withId(TASK_ID_1).build();
        Tuple mockTuple = mock(Tuple.class);

        when(taskRepository.saveWithClosure(any(Task.class), any())).thenReturn(savedTask);
        when(taskRepository.findTaskByIdWithRelations(TASK_ID_1)).thenReturn(Optional.of(mockTuple));

        TaskResponseDto result = taskService.createTask(dto);

        assertThat(result).isNotNull();
    }

    // ========== getTask() tests ==========

    @Test
    @DisplayName("getTask - should call findTaskByIdWithRelations")
    void getTask_shouldCallFindTaskByIdWithRelations() {
        Tuple mockTuple = mock(Tuple.class);
        when(taskRepository.findTaskByIdWithRelations(TASK_ID_1)).thenReturn(Optional.of(mockTuple));

        taskService.getTask(TASK_ID_1);

        verify(taskRepository).findTaskByIdWithRelations(TASK_ID_1);
    }

    @Test
    @DisplayName("getTask - should return task when found")
    void getTask_shouldReturnTaskWhenFound() {
        Tuple mockTuple = mock(Tuple.class);
        when(taskRepository.findTaskByIdWithRelations(TASK_ID_1)).thenReturn(Optional.of(mockTuple));

        TaskResponseDto result = taskService.getTask(TASK_ID_1);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("getTask - should throw when task not found")
    void getTask_shouldThrowWhenTaskNotFound() {
        when(taskRepository.findTaskByIdWithRelations(NON_EXISTENT_TASK_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTask(NON_EXISTENT_TASK_ID))
                .isInstanceOf(HandledException.class)
                .hasFieldOrPropertyWithValue("errorMessage", ErrorMessage.TASK_NOT_FOUND);
    }

    // ========== getAllTasks() tests ==========

    @Test
    @DisplayName("getAllTasks - should call findAllTasksWithRelations")
    void getAllTasks_shouldCallFindAllTasksWithRelations() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Tuple> page = new PageImpl<>(List.of());

        when(taskRepository.findAllTasksWithRelations(pageable)).thenReturn(page);

        taskService.getAllTasks(pageable);

        verify(taskRepository).findAllTasksWithRelations(pageable);
    }

    @Test
    @DisplayName("getAllTasks - should return paged results")
    void getAllTasks_shouldReturnPagedResults() {
        Pageable pageable = PageRequest.of(0, 10);
        Tuple mockTuple = mock(Tuple.class);
        Page<Tuple> page = new PageImpl<>(List.of(mockTuple));

        when(taskRepository.findAllTasksWithRelations(pageable)).thenReturn(page);

        Page<TaskResponseDto> result = taskService.getAllTasks(pageable);

        assertThat(result.getContent()).hasSize(1);
    }

    // ========== updateTask() tests ==========

    @Test
    @DisplayName("updateTask - should call findByIdAndIsActiveTrue")
    void updateTask_shouldCallFindByIdAndIsActiveTrue() {
        Task existingTask = aTask().withId(TASK_ID_1).build();
        TaskUpdateDto dto = aTaskUpdateDto().build();
        Tuple mockTuple = mock(Tuple.class);

        when(taskRepository.findByIdAndIsActiveTrue(TASK_ID_1)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);
        when(taskRepository.findTaskByIdWithRelations(TASK_ID_1)).thenReturn(Optional.of(mockTuple));

        taskService.updateTask(dto, TASK_ID_1);

        verify(taskRepository).findByIdAndIsActiveTrue(TASK_ID_1);
    }

    @Test
    @DisplayName("updateTask - should throw when task not found")
    void updateTask_shouldThrowWhenTaskNotFound() {
        TaskUpdateDto dto = aTaskUpdateDto().build();

        when(taskRepository.findByIdAndIsActiveTrue(NON_EXISTENT_TASK_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.updateTask(dto, NON_EXISTENT_TASK_ID))
                .isInstanceOf(HandledException.class)
                .hasFieldOrPropertyWithValue("errorMessage", ErrorMessage.TASK_NOT_FOUND);
    }

    @Test
    @DisplayName("updateTask - should not call save when task not found")
    void updateTask_shouldNotCallSaveWhenTaskNotFound() {
        TaskUpdateDto dto = aTaskUpdateDto().build();

        when(taskRepository.findByIdAndIsActiveTrue(NON_EXISTENT_TASK_ID)).thenReturn(Optional.empty());

        try {
            taskService.updateTask(dto, NON_EXISTENT_TASK_ID);
        } catch (HandledException e) {
            // Expected
        }

        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateTask - should validate status ID")
    void updateTask_shouldValidateStatusId() {
        Task existingTask = aTask().withId(TASK_ID_1).build();
        TaskUpdateDto dto = aTaskUpdateDto().withStatusId(INVALID_TASK_STATUS_ID).build();

        when(taskRepository.findByIdAndIsActiveTrue(TASK_ID_1)).thenReturn(Optional.of(existingTask));

        assertThatThrownBy(() -> taskService.updateTask(dto, TASK_ID_1))
                .isInstanceOf(HandledException.class)
                .hasFieldOrPropertyWithValue("errorMessage", ErrorMessage.TASK_STATUS_NOT_FOUND);
    }

    @Test
    @DisplayName("updateTask - should validate priority ID")
    void updateTask_shouldValidatePriorityId() {
        Task existingTask = aTask().withId(TASK_ID_1).build();
        TaskUpdateDto dto = aTaskUpdateDto().withPriorityId(INVALID_TASK_PRIORITY_ID).build();

        when(taskRepository.findByIdAndIsActiveTrue(TASK_ID_1)).thenReturn(Optional.of(existingTask));

        assertThatThrownBy(() -> taskService.updateTask(dto, TASK_ID_1))
                .isInstanceOf(HandledException.class)
                .hasFieldOrPropertyWithValue("errorMessage", ErrorMessage.TASK_PRIORITY_NOT_FOUND);
    }

    @Test
    @DisplayName("updateTask - should throw when transitioning from terminal status")
    void updateTask_shouldThrowWhenTransitioningFromTerminalStatus() {
        Task existingTask = aTask()
                .withId(TASK_ID_1)
                .withStatusId(TASK_STATUS_COMPLETED)
                .build();
        TaskUpdateDto dto = aTaskUpdateDto()
                .withStatusId(TASK_STATUS_IN_PROGRESS)
                .build();

        when(taskRepository.findByIdAndIsActiveTrue(TASK_ID_1)).thenReturn(Optional.of(existingTask));

        assertThatThrownBy(() -> taskService.updateTask(dto, TASK_ID_1))
                .isInstanceOf(HandledException.class)
                .hasFieldOrPropertyWithValue("errorMessage", ErrorMessage.INVALID_STATUS_TRANSITION);
    }

    @Test
    @DisplayName("updateTask - should not call save when invalid status transition")
    void updateTask_shouldNotCallSaveWhenInvalidStatusTransition() {
        Task existingTask = aTask()
                .withId(TASK_ID_1)
                .withStatusId(TASK_STATUS_COMPLETED)
                .build();
        TaskUpdateDto dto = aTaskUpdateDto()
                .withStatusId(TASK_STATUS_IN_PROGRESS)
                .build();

        when(taskRepository.findByIdAndIsActiveTrue(TASK_ID_1)).thenReturn(Optional.of(existingTask));

        try {
            taskService.updateTask(dto, TASK_ID_1);
        } catch (HandledException e) {
            // Expected
        }

        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateTask - should call updateTaskAndDescendantsStatus when transitioning to terminal")
    void updateTask_shouldCallUpdateDescendantsWhenTransitioningToTerminal() {
        Task existingTask = aTask()
                .withId(TASK_ID_1)
                .withStatusId(TASK_STATUS_IN_PROGRESS)
                .build();
        TaskUpdateDto dto = aTaskUpdateDto()
                .withStatusId(TASK_STATUS_COMPLETED)
                .build();
        Tuple mockTuple = mock(Tuple.class);

        when(taskRepository.findByIdAndIsActiveTrue(TASK_ID_1)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);
        when(taskRepository.findTaskByIdWithRelations(TASK_ID_1)).thenReturn(Optional.of(mockTuple));

        taskService.updateTask(dto, TASK_ID_1);

        verify(taskRepository).updateTaskAndDescendantsStatus(TASK_ID_1, TASK_STATUS_COMPLETED);
    }

    @Test
    @DisplayName("updateTask - should not call updateTaskAndDescendantsStatus when not changing to terminal")
    void updateTask_shouldNotCallUpdateDescendantsWhenNotChangingToTerminal() {
        Task existingTask = aTask()
                .withId(TASK_ID_1)
                .withStatusId(TASK_STATUS_ON_HOLD)
                .build();
        TaskUpdateDto dto = aTaskUpdateDto()
                .withStatusId(TASK_STATUS_IN_PROGRESS)
                .build();
        Tuple mockTuple = mock(Tuple.class);

        when(taskRepository.findByIdAndIsActiveTrue(TASK_ID_1)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);
        when(taskRepository.findTaskByIdWithRelations(TASK_ID_1)).thenReturn(Optional.of(mockTuple));

        taskService.updateTask(dto, TASK_ID_1);

        verify(taskRepository, never()).updateTaskAndDescendantsStatus(any(), any());
    }

    @Test
    @DisplayName("updateTask - should not call updateTaskAndDescendantsStatus when status unchanged")
    void updateTask_shouldNotCallUpdateDescendantsWhenStatusUnchanged() {
        Task existingTask = aTask()
                .withId(TASK_ID_1)
                .withStatusId(TASK_STATUS_IN_PROGRESS)
                .build();
        TaskUpdateDto dto = aTaskUpdateDto()
                .withStatusId(TASK_STATUS_IN_PROGRESS)
                .build();
        Tuple mockTuple = mock(Tuple.class);

        when(taskRepository.findByIdAndIsActiveTrue(TASK_ID_1)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);
        when(taskRepository.findTaskByIdWithRelations(TASK_ID_1)).thenReturn(Optional.of(mockTuple));

        taskService.updateTask(dto, TASK_ID_1);

        verify(taskRepository, never()).updateTaskAndDescendantsStatus(any(), any());
    }

    @Test
    @DisplayName("updateTask - should call save")
    void updateTask_shouldCallSave() {
        Task existingTask = aTask().withId(TASK_ID_1).build();
        TaskUpdateDto dto = aTaskUpdateDto().build();
        Tuple mockTuple = mock(Tuple.class);

        when(taskRepository.findByIdAndIsActiveTrue(TASK_ID_1)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);
        when(taskRepository.findTaskByIdWithRelations(TASK_ID_1)).thenReturn(Optional.of(mockTuple));

        taskService.updateTask(dto, TASK_ID_1);

        verify(taskRepository).save(any(Task.class));
    }

    @Test
    @DisplayName("updateTask - should return updated task")
    void updateTask_shouldReturnUpdatedTask() {
        Task existingTask = aTask().withId(TASK_ID_1).build();
        TaskUpdateDto dto = aTaskUpdateDto().build();
        Tuple mockTuple = mock(Tuple.class);

        when(taskRepository.findByIdAndIsActiveTrue(TASK_ID_1)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);
        when(taskRepository.findTaskByIdWithRelations(TASK_ID_1)).thenReturn(Optional.of(mockTuple));

        TaskResponseDto result = taskService.updateTask(dto, TASK_ID_1);

        assertThat(result).isNotNull();
    }

    // ========== deleteTask() tests ==========

    @Test
    @DisplayName("deleteTask - should throw when task not found")
    void deleteTask_shouldThrowWhenTaskNotFound() {
        when(taskRepository.findByIdAndIsActiveTrue(NON_EXISTENT_TASK_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.deleteTask(NON_EXISTENT_TASK_ID))
                .isInstanceOf(HandledException.class)
                .hasFieldOrPropertyWithValue("errorMessage", ErrorMessage.TASK_NOT_FOUND);
    }

    @Test
    @DisplayName("deleteTask - should not call deactivate when task not found")
    void deleteTask_shouldNotCallDeactivateWhenTaskNotFound() {
        when(taskRepository.findByIdAndIsActiveTrue(NON_EXISTENT_TASK_ID)).thenReturn(Optional.empty());

        try {
            taskService.deleteTask(NON_EXISTENT_TASK_ID);
        } catch (HandledException e) {
            // Expected
        }

        verify(taskRepository, never()).deactivateTaskAndDescendants(any());
    }

    @Test
    @DisplayName("deleteTask - should call deactivateTaskAndDescendants")
    void deleteTask_shouldCallDeactivateTaskAndDescendants() {
        Task existingTask = aTask().withId(TASK_ID_1).build();

        when(taskRepository.findByIdAndIsActiveTrue(TASK_ID_1)).thenReturn(Optional.of(existingTask));

        taskService.deleteTask(TASK_ID_1);

        verify(taskRepository).deactivateTaskAndDescendants(TASK_ID_1);
    }

    @Test
    @DisplayName("deleteTask - should call deactivateIssuesByAncestorTaskId")
    void deleteTask_shouldCallDeactivateIssuesByAncestorTaskId() {
        Task existingTask = aTask().withId(TASK_ID_1).build();

        when(taskRepository.findByIdAndIsActiveTrue(TASK_ID_1)).thenReturn(Optional.of(existingTask));

        taskService.deleteTask(TASK_ID_1);

        verify(issueService).deactivateIssuesByAncestorTaskId(TASK_ID_1);
    }

    // ========== taskExists() tests ==========

    @Test
    @DisplayName("taskExists - should return true when task exists")
    void taskExists_shouldReturnTrueWhenTaskExists() {
        Task existingTask = aTask().withId(TASK_ID_1).build();

        when(taskRepository.findByIdAndIsActiveTrue(TASK_ID_1)).thenReturn(Optional.of(existingTask));

        boolean result = taskService.taskExists(TASK_ID_1);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("taskExists - should return false when task not found")
    void taskExists_shouldReturnFalseWhenTaskNotFound() {
        when(taskRepository.findByIdAndIsActiveTrue(NON_EXISTENT_TASK_ID)).thenReturn(Optional.empty());

        boolean result = taskService.taskExists(NON_EXISTENT_TASK_ID);

        assertThat(result).isFalse();
    }
}
