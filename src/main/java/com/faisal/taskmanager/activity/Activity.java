package com.faisal.taskmanager.activity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;


@Entity
@Table(name = "activity_log")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
class Activity {

    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private UUID id;

    @Column(name = "created_at")
    @CreationTimestamp(source = SourceType.DB)
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp(source = SourceType.DB)
    private Instant updatedAt;

    @Column(name = "is_active")
    private Boolean isActive;

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
