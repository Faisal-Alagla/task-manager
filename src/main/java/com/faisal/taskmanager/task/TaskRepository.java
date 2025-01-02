package com.faisal.taskmanager.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    @Modifying
    @Query("UPDATE Task t SET t.isActive = false WHERE t.id = :id")
    void deleteById(UUID id);

}
