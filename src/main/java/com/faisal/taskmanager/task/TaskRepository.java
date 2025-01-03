package com.faisal.taskmanager.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    @Query(
            """
               SELECT new com.faisal.taskmanager.task.TaskResponseDto(
                   t.id,
                   t.name,
                   t.assigneeId,
                   t.dueDate,
                   t.description,
                   t.statusId,
                   t.priorityId,
                   ARRAY_AGG(i.id),
                   t.isActive
               )
               FROM Task t
               LEFT JOIN Issue i ON i.taskId = t.id
               WHERE t.id = :taskId
               GROUP BY t.id, t.name, t.assigneeId, t.dueDate, t.description, t.statusId, t.priorityId, t.isActive
               """
    )
    Optional<TaskResponseDto> findTaskByIdWithIssueIds(UUID taskId);

    @Modifying
    @Query("UPDATE Task t SET t.isActive = false WHERE t.id = :id")
    void deleteById(UUID id);

}
