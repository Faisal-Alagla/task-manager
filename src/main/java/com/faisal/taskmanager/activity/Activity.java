package com.faisal.taskmanager.activity;

import com.faisal.taskmanager.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;


@Entity
@Table(name = "activity_log")
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
class Activity extends BaseEntity {

    @Column(name = "http_method")
    private String httpMethod;

    @Column(name = "uri")
    private String uri;

    @Column(name = "operation")
    private String operation;

    @Column(name = "operation_reference_id")
    private String operationReferenceId;

    @Column(name = "message")
    private String message;

    @Column(name = "user_id")
    private UUID userId;

}
