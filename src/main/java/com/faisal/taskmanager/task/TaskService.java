package com.faisal.taskmanager.task;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService implements ITaskService {

    private final TaskRepository taskRepository;

    @Override
    public TaskResponseDto createTask(TaskCreationDto taskCreationDto) {
        Task createdTask = taskRepository.save(TaskMapper.mapToTask(taskCreationDto));

        return TaskMapper.mapToTaskResponseDto(createdTask);
    }

    @Override
    public TaskResponseDto getTask(UUID taskId) {
        return taskRepository.findTaskByIdWithIssueIds(taskId)
                .orElseThrow(RuntimeException::new); //TODO: to be changed with custom exception
    }

    @Override
    public Page<TaskResponseDto> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable).map(TaskMapper::mapToTaskResponseDto);
    }

    @Override
    public TaskResponseDto updateTask(TaskUpdateDto taskUpdateDto, UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(RuntimeException::new); //TODO: to be changed with custom exception

        updateTaskData(task, taskUpdateDto);
        Task updatedTask = taskRepository.save(task);

        return TaskMapper.mapToTaskResponseDto(updatedTask);
    }

    @Override
    public void deleteTask(UUID taskId) {
        taskRepository.findById(taskId)
                .orElseThrow(RuntimeException::new); //TODO: to be changed with custom exception

        taskRepository.deleteById(taskId);
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
