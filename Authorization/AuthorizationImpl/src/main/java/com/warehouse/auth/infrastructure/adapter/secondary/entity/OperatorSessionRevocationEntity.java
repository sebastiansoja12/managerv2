package com.warehouse.auth.infrastructure.adapter.secondary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "operator_session_revocations")
public class OperatorSessionRevocationEntity {

    @Id
    @Column(name = "operator_id", nullable = false)
    private Long operatorId;

    @Column(name = "revoked_at", nullable = false)
    private Instant revokedAt;
}
