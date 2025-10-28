package com.faisal.taskmanager.task;

import com.faisal.taskmanager.task.dto.TaskCreationDto;
import com.faisal.taskmanager.task.dto.TaskResponseDto;
import com.faisal.taskmanager.task.dto.TaskUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ITaskService {

    /**
     * Creates a new task
     *
     * @param taskCreationDto TaskCreationDto object containing the task data to be created
     * @return TaskResponseDto containing the created task data
     */
    TaskResponseDto createTask(TaskCreationDto taskCreationDto);

    /**
     * Get a task by id
     *
     * @param taskId id of the task to be fetched
     * @return TaskResponseDto object containing the task data
     */
    TaskResponseDto getTask(UUID taskId);

    /**
     * Get all active tasks (paginated)
     *
     * @return a Page<TaskResponseDto> containing the tasks data
     */
    Page<TaskResponseDto> getAllTasks(Pageable pageable);

    /**
     * Update a task by id
     *
     * @param taskUpdateDto TaskUpdateDto object containing the new task data
     * @param taskId  id of the task to be updated
     * @return TaskResponseDto object containing the updated task data
     */
    TaskResponseDto updateTask(TaskUpdateDto taskUpdateDto, UUID taskId);

    /**
     * Soft delete a task by id
     *
     * @param taskId id of the task to be deleted
     */
    void deleteTask(UUID taskId);

    /**
     * Checks whether a task exists and active
     *
     * @param taskId id of the task to be checked
     * @return true if the task exists and active, false otherwise
     */
    boolean taskExists(UUID taskId);

}
