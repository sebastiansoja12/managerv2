package com.warehouse.reroute.infrastructure.adapter.secondary.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

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

    @Column(name = "token", nullable = false, updatable = false)
    private Integer token;

    @Column(name = "created", updatable = false)
    private Instant createdDate;

    @Column(name = "timeout", updatable = false)
    private Instant expiryDate;

    @Column(nullable = false, updatable = false)
    private Long parcelId;

}
