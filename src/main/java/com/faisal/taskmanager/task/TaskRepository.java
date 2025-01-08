package com.faisal.taskmanager.task;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TaskRepository {

    private final TaskJpa taskJpa;

    @PersistenceContext
    private EntityManager entityManager;

    public Task save(Task task) {
        return taskJpa.save(task);
    }

    public Page<Task> findAll(Pageable pageable) {
        return taskJpa.findAll(pageable);
    }

    public Optional<Task> findById(UUID id) {
        return taskJpa.findById(id);
    }

    Optional<TaskResponseDto> findTaskByIdWithIssueIds(UUID taskId) {

        //FIXME: bugged
        String query = """
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
                """;

        return (Optional<TaskResponseDto>) entityManager.createNativeQuery(query, TaskResponseDto.class)
                .setParameter("taskId", taskId)
                .getSingleResult();
    }

    void deactivateTaskAndIssues(UUID taskId) {
        taskJpa.deactivateTaskAndIssues(taskId);
    }

}
