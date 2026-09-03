package com.warehouse.reroute.infrastructure.adapter.secondary.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "reroute_token")
@Entity(name = "reroute.RerouteTokenEntity")
public class RerouteTokenEntity {

    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false, updatable = false)
    private Integer token;

    @Column(name = "created", updatable = false)
    private Instant createdAt;

    @Column(name = "timeout")
    private Instant expiresAt;

    @Column(name = "parcel_id", nullable = false, updatable = false)
    private Long shipmentId;

    @Column(name = "email", nullable = false, updatable = false)
    private String email;

    @Column(name = "failed_attempts", nullable = false)
    private Integer failedAttempts;

    @Column(name = "used_at")
    private Instant usedAt;
}
