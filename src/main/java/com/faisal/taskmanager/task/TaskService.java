package com.faisal.taskmanager.task;

import com.faisal.taskmanager.issue.Issue;
import com.faisal.taskmanager.issue.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService implements ITaskService {

    private final TaskRepository taskRepository;
    private final IssueRepository issueRepository;

    @Override
    public TaskResponseDto createTask(TaskCreationDto taskCreationDto) {
        Task createdTask = taskRepository.save(TaskMapper.mapToTask(taskCreationDto));

        return TaskMapper.mapToTaskResponseDto(createdTask);
    }

    @Override
    public TaskResponseDto getTask(UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(RuntimeException::new); //TODO: to be changed with custom exception

        TaskResponseDto taskResponseDto = TaskMapper.mapToTaskResponseDto(task);
        taskResponseDto.setIssuesIds(
                issueRepository.findAllByTaskId(
                        taskId
                ).stream().map(Issue::getId).toList()
        );

        return taskResponseDto;
    }

    @Override
    public List<TaskResponseDto> getAllTasks() {
        return taskRepository.findAll().stream().map(TaskMapper::mapToTaskResponseDto).toList();
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
        Task task = taskRepository.findById(taskId)
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
