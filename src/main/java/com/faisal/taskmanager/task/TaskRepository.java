package com.faisal.taskmanager.task;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    public Optional<Task> findByIdAndIsActiveTrue(UUID id) {
        return taskJpaRepository.findByIdAndIsActiveTrue(id);
    }

    void deactivateTaskAndIssues(UUID taskId) {
        taskJpaRepository.deactivateTaskAndIssues(taskId);
    }
    //endregion

    //region EntityManager methods
    Tuple findTaskByIdWithIssueIds(UUID taskId) {
        String query = buildTaskWithIssueIdsQuery(true);

        return (Tuple) entityManager.createNativeQuery(query, Tuple.class)
                .setParameter("taskId", taskId)
                .getSingleResult();
    }

    public Page<Tuple> findAllTasksWithIssueIds(Pageable pageable) {
        String query = buildTaskWithIssueIdsQuery(false);

        String countQuery = """
            SELECT COUNT(*)
            FROM task t
            WHERE t.is_active = true
            """;

        long total = ((Number) entityManager.createNativeQuery(countQuery)
                .getSingleResult()).longValue();

        List<Tuple> results = entityManager.createNativeQuery(query, Tuple.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(results, pageable, total);
    }

    private String buildTaskWithIssueIdsQuery(boolean includeTaskIdFilter) {
        String baseQuery = """
            SELECT
                t.id AS id,
                t.name AS name,
                t.assignee_id AS assigneeId,
                t.due_date AS dueDate,
                t.description AS description,
                t.status_id AS statusId,
                t.priority_id AS priorityId,
                ARRAY_AGG(i.id) AS issuesIds
            FROM task t
            LEFT JOIN issue i ON i.task_id = t.id AND i.is_active = true
            WHERE t.is_active = true
            """;

        if (includeTaskIdFilter) {
            baseQuery += " AND t.id = :taskId ";
        }

        return baseQuery + """
            GROUP BY t.id, t.name, t.assignee_id, t.due_date, t.description, t.status_id, t.priority_id, t.is_active
            """;
    }
    //endregion

}
