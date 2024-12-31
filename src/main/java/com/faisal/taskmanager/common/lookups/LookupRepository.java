package com.faisal.taskmanager.common.lookups;

import com.faisal.taskmanager.common.lookups.entities.IssueCriticalityLk;
import com.faisal.taskmanager.common.lookups.entities.IssueStatusLk;
import com.faisal.taskmanager.common.lookups.entities.TaskPriorityLk;
import com.faisal.taskmanager.common.lookups.entities.TaskStatusLk;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
class LookupRepository {

    @PersistenceContext
    private EntityManager entityManager;

    List<IssueCriticalityLk> getIssueCriticalityLookup() {
        TypedQuery<IssueCriticalityLk> query = entityManager.createQuery(
                "SELECT ic FROM IssueCriticalityLk as ic",
                IssueCriticalityLk.class
        );
        return query.getResultList();
    }

    List<IssueStatusLk> getIssueStatusLookup() {
        TypedQuery<IssueStatusLk> query = entityManager.createQuery(
                "SELECT iss FROM IssueStatusLk as iss",
                IssueStatusLk.class
        );
        return query.getResultList();
    }

    List<TaskPriorityLk> getTaskPriorityLookup() {
        TypedQuery<TaskPriorityLk> query = entityManager.createQuery(
                "SELECT tp FROM TaskPriorityLk as tp",
                TaskPriorityLk.class
        );
        return query.getResultList();
    }

    List<TaskStatusLk> getTaskStatusLookup() {
        TypedQuery<TaskStatusLk> query = entityManager.createQuery(
                "SELECT ts FROM TaskStatusLk as ts",
                TaskStatusLk.class
        );
        return query.getResultList();
    }

}
