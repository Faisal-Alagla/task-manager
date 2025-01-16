package com.faisal.taskmanager.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TaskJpaRepository extends JpaRepository<Task, UUID> {

    Optional<Task> findByIdAndIsActiveTrue(UUID taskId);

}
