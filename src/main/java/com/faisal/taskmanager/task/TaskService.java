package com.faisal.taskmanager.task;

import com.faisal.taskmanager.common.exceptions.ErrorMessage;
import com.faisal.taskmanager.common.exceptions.ResourceException;
import com.faisal.taskmanager.common.lookups.LookupResponseDto;
import com.faisal.taskmanager.common.lookups.LookupService;
import com.faisal.taskmanager.common.lookups.LookupType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService implements ITaskService {

    private final TaskRepository taskRepository;
    private final LookupService lookupService;

    @Override
    @Transactional
    public TaskResponseDto createTask(TaskCreationDto taskCreationDto) {
        Task createdTask = taskRepository.saveWithClosure(
                TaskMapper.mapToTask(taskCreationDto),
                taskCreationDto.getParentTaskId()
        );

        return taskRepository.findTaskByIdWithRelations(createdTask.getId())
                .map(TaskMapper::mapToTaskResponseFromTuple)
                .orElseThrow(() -> new ResourceException(ErrorMessage.TASK_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponseDto getTask(UUID taskId) {
        return taskRepository.findTaskByIdWithRelations(taskId)
                .map(TaskMapper::mapToTaskResponseFromTuple)
                .orElseThrow(() -> new ResourceException(ErrorMessage.TASK_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskResponseDto> getAllTasks(Pageable pageable) {
        return taskRepository.findAllTasksWithRelations(pageable)
                .map(TaskMapper::mapToTaskResponseFromTuple);
    }

    @Override
    @Transactional
    public TaskResponseDto updateTask(TaskUpdateDto taskUpdateDto, UUID taskId) {
        Task task = taskRepository.findByIdAndIsActiveTrue(taskId)
                .orElseThrow(() -> new ResourceException(ErrorMessage.TASK_NOT_FOUND));

        Integer oldStatus = task.getStatusId();
        Integer newStatus = taskUpdateDto.getStatusId();
        boolean isStatusChanged = !Objects.equals(oldStatus, newStatus);

        if (isStatusChanged) {
            LookupResponseDto newStatusLookup = lookupService.findLookupById(LookupType.TASK_STATUS, newStatus)
                    .orElseThrow(() -> new ResourceException(ErrorMessage.TASK_STATUS_NOT_FOUND));

            String statusName = newStatusLookup.getName();

            if ("completed".equals(statusName) || "cancelled".equals(statusName)) {
                taskRepository.updateTaskAndDescendantsStatus(taskId, newStatus);
            }
        }

        updateTaskData(task, taskUpdateDto);
        taskRepository.save(task);

        return taskRepository.findTaskByIdWithRelations(taskId)
                .map(TaskMapper::mapToTaskResponseFromTuple)
                .orElseThrow(() -> new ResourceException(ErrorMessage.TASK_NOT_FOUND));
    }

    @Override
    @Transactional
    public void deleteTask(UUID taskId) {
        if (!taskExists(taskId)) {
            throw new ResourceException(ErrorMessage.TASK_NOT_FOUND);
        }

        taskRepository.deactivateTaskAndDescendants(taskId);
    }

    public boolean taskExists(UUID taskId) {
        return taskRepository.findByIdAndIsActiveTrue(taskId).isPresent();
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
}
