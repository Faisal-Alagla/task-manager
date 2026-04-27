package com.faisal.taskmanager.task;

import com.faisal.taskmanager.common.exceptions.ErrorMessage;
import com.faisal.taskmanager.common.exceptions.HandledException;
import com.faisal.taskmanager.issue.IIssueService;
import com.faisal.taskmanager.task.dto.TaskCreationDto;
import com.faisal.taskmanager.task.dto.TaskCreateResponseDto;
import com.faisal.taskmanager.task.dto.TaskResponseDto;
import com.faisal.taskmanager.task.dto.TaskUpdateDto;
import com.faisal.taskmanager.task.validator.TaskValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService implements ITaskService {

    private final TaskRepository taskRepository;
    private final IIssueService issueService;
    private final TaskValidator taskValidator;
    private final TaskLookupContext taskLookupContext;
    private final TaskAssigneeValidationGateway taskAssigneeValidationGateway;

    @Override
    @Transactional
    public TaskCreateResponseDto createTask(TaskCreationDto taskCreationDto) {
        taskValidator.validateParentTaskExists(taskCreationDto.getParentTaskId());
        taskValidator.validateTaskCreationLookups(taskCreationDto.getStatusId(), taskCreationDto.getPriorityId());
        TaskAssigneeValidationOutcome assigneeValidationOutcome = taskCreationDto.getAssigneeId() == null
                ? TaskAssigneeValidationOutcome.resolvedAssignee(null)
                : taskAssigneeValidationGateway.validateAssignee(taskCreationDto.getAssigneeId());

        Task createdTask = taskRepository.saveWithClosure(
                TaskMapper.mapToTask(taskCreationDto, assigneeValidationOutcome.resolvedAssigneeId()),
                taskCreationDto.getParentTaskId()
        );

        TaskResponseDto createdTaskResponse = taskRepository.findTaskByIdWithRelations(createdTask.getId())
                .map(TaskMapper::mapToTaskResponseFromTuple)
                .orElseThrow(() -> new HandledException(ErrorMessage.TASK_NOT_FOUND));

        return new TaskCreateResponseDto(createdTaskResponse, assigneeValidationOutcome.warning());
    }

    @Override
    public TaskResponseDto getTask(UUID taskId) {
        return taskRepository.findTaskByIdWithRelations(taskId)
                .map(TaskMapper::mapToTaskResponseFromTuple)
                .orElseThrow(() -> new HandledException(ErrorMessage.TASK_NOT_FOUND));
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
                .orElseThrow(() -> new HandledException(ErrorMessage.TASK_NOT_FOUND));

        taskValidator.validateTaskUpdateLookups(taskUpdateDto.getStatusId(), taskUpdateDto.getPriorityId());

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
                .orElseThrow(() -> new HandledException(ErrorMessage.TASK_NOT_FOUND));
    }

    @Override
    @Transactional
    public void deleteTask(UUID taskId) {
        if (!taskExists(taskId)) {
            throw new HandledException(ErrorMessage.TASK_NOT_FOUND);
        }

        // Deactivate the task and all its descendants
        taskRepository.deactivateTaskAndDescendants(taskId);

        // Deactivate all issues linked to the task and its descendants
        issueService.deactivateIssuesByAncestorTaskId(taskId);
    }

    public boolean taskExists(UUID taskId) {
        return taskRepository.findByIdAndIsActiveTrue(taskId).isPresent();
    }

    private void handleStatusChange(UUID taskId, Integer oldStatusId, Integer newStatusId) {
        if (!canTransitionToStatus(oldStatusId, newStatusId)) {
            throw new HandledException(ErrorMessage.INVALID_STATUS_TRANSITION);
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
        task.setUpdatedAt(Instant.now());
        task.setName(taskUpdateDto.getName());
        task.setAssigneeId(taskUpdateDto.getAssigneeId());
        task.setDueDate(taskUpdateDto.getDueDate());
        task.setDescription(taskUpdateDto.getDescription());
        task.setStatusId(taskUpdateDto.getStatusId());
        task.setPriorityId(taskUpdateDto.getPriorityId());
    }
}
