package com.warehouse.redirect.infrastructure.adapter.secondary.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "redirect_token")
@Entity(name = "redirect.RedirectTokenEntity")
public class RedirectTokenEntity {

    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "created", updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "timeout")
    private LocalDateTime expiryDate;

    @Column(name = "parcel_id", nullable = false, updatable = false)
    private Long parcelId;

    @Column(name = "email", nullable = false)
    private String email;
}
