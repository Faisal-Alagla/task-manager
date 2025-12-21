package com.faisal.taskmanager.common.lookups.entities;

import com.faisal.taskmanager.common.lookups.BaseLookupEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "task_priority_lk")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TaskPriorityLk extends BaseLookupEntity {

}
