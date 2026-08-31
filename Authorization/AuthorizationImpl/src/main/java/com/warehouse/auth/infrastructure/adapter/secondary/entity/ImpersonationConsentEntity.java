package com.warehouse.auth.infrastructure.adapter.secondary.entity;

import com.warehouse.auth.domain.model.ImpersonationConsentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "impersonation_consents")
public class ImpersonationConsentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "operator_id", nullable = false)
    private Long operatorId;

    @Column(name = "platform_owner_id", nullable = false)
    private Long platformOwnerId;

    @Column(name = "platform_owner_username")
    private String platformOwnerUsername;

    @Column(name = "reason", length = 500)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ImpersonationConsentStatus status;

    @Column(name = "requested_at", nullable = false)
    private Instant requestedAt;

    @Column(name = "request_expires_at", nullable = false)
    private Instant requestExpiresAt;

    @Column(name = "decided_by")
    private Long decidedBy;

    @Column(name = "decided_by_username")
    private String decidedByUsername;

    @Column(name = "decided_at")
    private Instant decidedAt;

    @Column(name = "access_expires_at")
    private Instant accessExpiresAt;
}
