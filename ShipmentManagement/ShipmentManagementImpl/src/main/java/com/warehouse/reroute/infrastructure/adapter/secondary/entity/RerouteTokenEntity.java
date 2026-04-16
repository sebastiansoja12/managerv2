package com.warehouse.reroute.infrastructure.adapter.secondary.entity;

import java.time.Instant;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reroute_token")
@Entity(name = "reroute.RerouteTokenEntity")
public class RerouteTokenEntity {

    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false)
    private Integer token;

    @Column(name = "created", updatable = false)
    private Instant createdDate;

    @Column(name = "timeout")
    private Instant expiryDate;

    @Column(name = "parcel_id", nullable = false, updatable = false)
    private Long parcelId;

    @Column(name = "email", nullable = false)
    private String email;
}
