package com.faisal.taskmanager.task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ITaskService {

    /**
     * Creates a new Task and store it in the DB
     *
     * @param taskCreationDto TaskCreationDto object containing the Task data to be created
     * @return TaskResponseDto containing the created Task data in the DB
     */
    TaskResponseDto createTask(TaskCreationDto taskCreationDto);

    /**
     * Get the Task from the DB with the matching Task id
     *
     * @param taskId id of the task to be fetched
     * @return TaskResponseDto object containing the created Task data
     */
    TaskResponseDto getTask(UUID taskId);

    /**
     * Gets all Tasks from the DB
     *
     * @return a List<TaskResponseDto> containing the tasks data
     */
    Page<TaskResponseDto> getAllTasks(Pageable pageable);

    /**
     * Updates an existing Task in the DB with the matching Task id
     *
     * @param taskUpdateDto TaskUpdateDto object containing the new updated Task data
     * @param taskId  id of the task to be updated
     * @return TaskResponseDto object containing the updated Task data
     */
    TaskResponseDto updateTask(TaskUpdateDto taskUpdateDto, UUID taskId);

    /**
     * Soft deletes a Task in the DB with the matching Task id
     *
     * @param taskId id of the Task to be deleted
     */
    void deleteTask(UUID taskId);

    /**
     * Checks whether a Task exists and active
     *
     * @param taskId id of the Task to be deleted
     * @return true if the task exists and active, false otherwise
     */
    boolean taskExists(UUID taskId);

}
