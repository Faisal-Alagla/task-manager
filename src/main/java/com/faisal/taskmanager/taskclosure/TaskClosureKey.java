package com.faisal.taskmanager.taskclosure;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskClosureKey implements Serializable {

    @Column(name = "ancestor_task_id", insertable = false, updatable = false)
    private UUID ancestorTaskId;

    @Column(name = "descendant_task_id", insertable = false, updatable = false)
    private UUID descendantTaskId;

}
