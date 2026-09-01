package com.warehouse.platformadministration.infrastructure.adapter.secondary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "platform_audit_log")
public class PlatformAuditEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "platform_owner_id", nullable = false)
    private Long platformOwnerId;

    @Column(name = "platform_owner_username", nullable = false)
    private String platformOwnerUsername;

    @Column(name = "operator_id")
    private Long operatorId;

    @Column(name = "impersonated_user_id")
    private Long impersonatedUserId;

    @Column(name = "impersonation", nullable = false)
    private boolean impersonation;

    @Column(name = "http_method", nullable = false, length = 10)
    private String httpMethod;

    @Column(name = "path", nullable = false, length = 500)
    private String path;

    @Column(name = "status_code", nullable = false)
    private int statusCode;

    @Column(name = "success", nullable = false)
    private boolean success;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
