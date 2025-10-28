package com.faisal.taskmanager.task;

import com.faisal.taskmanager.common.exceptions.ErrorMessage;
import com.faisal.taskmanager.common.exceptions.ResourceException;
import com.faisal.taskmanager.common.lookups.LookupService;
import com.faisal.taskmanager.common.lookups.domain.TaskPriorityLookupCollection;
import com.faisal.taskmanager.common.lookups.domain.TaskStatusLookupCollection;
import com.faisal.taskmanager.common.lookups.entities.TaskPriorityLk;
import com.faisal.taskmanager.common.lookups.entities.TaskStatusLk;
import com.faisal.taskmanager.task.dto.TaskCreationDto;
import com.faisal.taskmanager.task.dto.TaskResponseDto;
import com.faisal.taskmanager.task.dto.TaskUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService implements ITaskService {

    private final TaskRepository taskRepository;
    private final LookupService lookupService;

    // lookup collections
    private TaskStatusLookupCollection statuses;
    private TaskPriorityLookupCollection priorities;

    // lookup context
    private TaskLookupContext taskLookupContext;

    @PostConstruct
    private void init() {
        this.statuses = lookupService.getTaskStatusCollection();
        this.priorities = lookupService.getTaskPriorityCollection();

        this.taskLookupContext = buildLookupContext();
    }

    @Override
    public TaskResponseDto createTask(TaskCreationDto taskCreationDto) {
        validateTaskLookups(taskCreationDto.getStatusId(), taskCreationDto.getPriorityId());

        Task createdTask = taskRepository.saveWithClosure(
                TaskMapper.mapToTask(taskCreationDto),
                taskCreationDto.getParentTaskId()
        );

        return taskRepository.findTaskByIdWithRelations(createdTask.getId())
                .map(TaskMapper::mapToTaskResponseFromTuple)
                .orElseThrow(() -> new ResourceException(ErrorMessage.TASK_NOT_FOUND));
    }

    @Override
    public TaskResponseDto getTask(UUID taskId) {
        return taskRepository.findTaskByIdWithRelations(taskId)
                .map(TaskMapper::mapToTaskResponseFromTuple)
                .orElseThrow(() -> new ResourceException(ErrorMessage.TASK_NOT_FOUND));
    }

    @Override
    public Page<TaskResponseDto> getAllTasks(Pageable pageable) {
        return taskRepository.findAllTasksWithRelations(pageable)
                .map(TaskMapper::mapToTaskResponseFromTuple);
    }

    @Override
    @Transactional
    public TaskResponseDto updateTask(TaskUpdateDto taskUpdateDto, UUID taskId) {
        Task task = taskRepository.findByIdAndIsActiveTrue(taskId)
                .orElseThrow(() -> new ResourceException(ErrorMessage.TASK_NOT_FOUND));

        validateTaskLookups(taskUpdateDto.getStatusId(), taskUpdateDto.getPriorityId());

        Integer oldStatus = task.getStatusId();
        Integer newStatus = taskUpdateDto.getStatusId();
        boolean isStatusChanged = !Objects.equals(oldStatus, newStatus);

        if (isStatusChanged) {
            handleStatusChange(taskId, oldStatus, newStatus);
        }

        updateTaskData(task, taskUpdateDto);
        taskRepository.save(task);

        return taskRepository.findTaskByIdWithRelations(taskId)
                .map(TaskMapper::mapToTaskResponseFromTuple)
                .orElseThrow(() -> new ResourceException(ErrorMessage.TASK_NOT_FOUND));
    }

    @Override
    public void deleteTask(UUID taskId) {
        if (!taskExists(taskId)) {
            throw new ResourceException(ErrorMessage.TASK_NOT_FOUND);
        }

        taskRepository.deactivateTaskAndDescendants(taskId);
    }

    public boolean taskExists(UUID taskId) {
        return taskRepository.findByIdAndIsActiveTrue(taskId).isPresent();
    }

    private void handleStatusChange(UUID taskId, Integer oldStatusId, Integer newStatusId) {
        if (!canTransitionToStatus(oldStatusId, newStatusId)) {
            throw new ResourceException(ErrorMessage.INVALID_STATUS_TRANSITION);
        }

        // If transitioning to terminal status, update descendants
        if (taskLookupContext.getTerminalStatusIds().contains(newStatusId)) {
            taskRepository.updateTaskAndDescendantsStatus(taskId, newStatusId);
        }
    }

    private boolean canTransitionToStatus(Integer currentStatusId, Integer newStatusId) {
        if (Objects.equals(currentStatusId, newStatusId)) {
            return false;
        }

        // Can't transition from terminal status
        return !taskLookupContext.getTerminalStatusIds().contains(currentStatusId);
    }

    private void updateTaskData(Task task, TaskUpdateDto taskUpdateDto) {
        task.setUpdatedAt(LocalDateTime.now());
        task.setName(taskUpdateDto.getName());
        task.setAssigneeId(taskUpdateDto.getAssigneeId());
        task.setDueDate(taskUpdateDto.getDueDate());
        task.setDescription(taskUpdateDto.getDescription());
        task.setStatusId(taskUpdateDto.getStatusId());
        task.setPriorityId(taskUpdateDto.getPriorityId());
    }

    private void validateTaskLookups(Integer statusId, Integer priorityId) {
        if (!taskLookupContext.getTaskStatusIds().contains(statusId)) {
            throw new ResourceException(ErrorMessage.TASK_STATUS_NOT_FOUND);
        }

        if (!taskLookupContext.getTaskPriorityIds().contains(priorityId)) {
            throw new ResourceException(ErrorMessage.TASK_PRIORITY_NOT_FOUND);
        }
    }

    private TaskLookupContext buildLookupContext() {
        return TaskLookupContext.builder()
                // Task status IDs
                .taskStatusIds(
                        statuses.stream()
                                .map(TaskStatusLk::getId)
                                .collect(Collectors.toSet())
                )
                .terminalStatusIds(
                        statuses.getTerminalStatuses().stream()
                                .map(TaskStatusLk::getId)
                                .collect(Collectors.toSet())
                )

                // Task priority IDs
                .taskStatusIds(
                        priorities.stream()
                                .map(TaskPriorityLk::getId)
                                .collect(Collectors.toSet())
                )

                .build();
    }
}