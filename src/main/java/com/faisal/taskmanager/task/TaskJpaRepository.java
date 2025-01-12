package com.faisal.taskmanager.task;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface TaskJpaRepository extends JpaRepository<Task, UUID> {

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
