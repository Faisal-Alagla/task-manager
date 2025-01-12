package com.faisal.taskmanager.task;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TaskRepository {

    //Delegate to TaskJpaRepository for straightforward CRUD operations and generated queries
    private final TaskJpaRepository taskJpaRepository;

    //Use EntityManager for advanced or custom queries that cannot be handled by JpaRepository
    @PersistenceContext
    private EntityManager entityManager;

    //region TaskJpaRepository methods
    public Task save(Task task) {
        return taskJpaRepository.save(task);
    }

    public Page<Task> findAll(Pageable pageable) {
        return taskJpaRepository.findAll(pageable);
    }

    public Optional<Task> findById(UUID id) {
        return taskJpaRepository.findById(id);
    }

    void deactivateTaskAndIssues(UUID taskId) {
        taskJpaRepository.deactivateTaskAndIssues(taskId);
    }
    //endregion

    //region EntityManager methods
    Tuple findTaskByIdWithIssueIds(UUID taskId) {

        String query = """
                SELECT
                    t.id AS id,
                    t.name as name,
                    t.assignee_id AS assigneeId,
                    t.due_date AS dueDate,
                    t.description AS description,
                    t.status_id AS statusId,
                    t.priority_id AS priorityId,
                    ARRAY_AGG(i.id) AS issuesIds
                FROM task t
                LEFT JOIN issue i ON i.task_Id = t.id AND i.is_active = true
                WHERE t.id = :taskId AND t.is_active = true
                GROUP BY t.id, t.name, t.assignee_id, t.due_date, t.description, t.status_id, t.priority_id, t.is_active
                """;

        return (Tuple) entityManager.createNativeQuery(query, Tuple.class)
                .setParameter("taskId", taskId)
                .getSingleResult();
    }
    //endregion

}
