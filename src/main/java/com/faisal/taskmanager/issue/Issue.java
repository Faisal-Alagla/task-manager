package com.faisal.taskmanager.issue;

import com.faisal.taskmanager.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(name = "issue")
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
public class Issue extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "status_id")
    private Integer statusId;

    @Column(name = "criticality_id")
    private Integer criticalityId;

    @Column(name = "task_id")
    private UUID taskId;

}
