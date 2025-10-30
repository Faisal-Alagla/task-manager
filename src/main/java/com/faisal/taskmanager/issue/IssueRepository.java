package com.faisal.taskmanager.issue;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface IssueRepository extends JpaRepository<Issue, UUID> {

    Optional<Issue> findByIdAndIsActiveTrue(UUID issueId);

    @Transactional
    @Modifying
    @Query("UPDATE Issue i SET i.isActive = false WHERE i.id = :issueId")
    void deactivateIssue(UUID issueId);

    @Transactional
    @Modifying
    @Query(value = """
            UPDATE issue
            SET is_active = false,
                updated_at = CURRENT_TIMESTAMP
            WHERE task_id IN (
                SELECT descendant_task_id
                FROM task_closure
                WHERE ancestor_task_id = :ancestorTaskId
            )
            """, nativeQuery = true)
    void deactivateIssuesByAncestorTaskId(UUID ancestorTaskId);

}
