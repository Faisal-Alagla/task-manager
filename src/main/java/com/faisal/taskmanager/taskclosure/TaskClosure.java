package com.faisal.taskmanager.taskclosure;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
