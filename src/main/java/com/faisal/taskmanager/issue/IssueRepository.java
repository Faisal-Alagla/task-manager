package com.faisal.taskmanager.issue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface IssueRepository extends JpaRepository<Issue, UUID> {

    List<Issue> findAllByTaskId(UUID taskId);

    @Modifying
    @Query("UPDATE Issue i SET i.isActive = false WHERE i.id = :id")
    void deleteById(UUID id);
}
