package com.faisal.taskmanager.task;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    @Query(
            //TODO: to be fixed...
            value = """
                    SELECT new com.faisal.taskmanager.task.TaskResponseDto(
                        t.id,
                        t.name,
                        t.assigneeId,
                        t.dueDate,
                        t.description,
                        t.statusId,
                        t.priorityId,
                        ARRAY_AGG(i.id)
                    )
                    FROM Task t
                    LEFT JOIN Issue i ON i.taskId = t.id AND i.is_active = true
                    WHERE t.id = :taskId AND t.is_active = true
                    GROUP BY t.id, t.name, t.assigneeId, t.dueDate, t.description, t.statusId, t.priorityId, t.isActive
                    """,
            nativeQuery = true
    )
    Optional<TaskResponseDto> findTaskByIdWithIssueIds(UUID taskId);

    @Transactional
    @Modifying
    @Query(value = """
            WITH update_task AS (
                UPDATE task
                SET is_active = FALSE
                WHERE id = :taskId
                RETURNING id
            )
            UPDATE issue
            SET is_active = FALSE
            WHERE task_id IN (SELECT id FROM update_task)
            """,
            nativeQuery = true)
    void deactivateTaskAndIssues(UUID taskId);

}
