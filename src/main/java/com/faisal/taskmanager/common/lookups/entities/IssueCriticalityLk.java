package com.faisal.taskmanager.common.lookups.entities;

import com.faisal.taskmanager.utils.baseclasses.BaseLookupEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "issue_criticality_lk")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class IssueCriticalityLk extends BaseLookupEntity {

}
