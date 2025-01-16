package com.faisal.taskmanager.task;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "task_closure")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskClosure {

    @EmbeddedId
    private TaskClosureKey id;

    @Transient
    private UUID ancestorTaskId;

    @Transient
    private UUID descendantTaskId;

    @Column(name = "depth")
    private Integer depth;

}
