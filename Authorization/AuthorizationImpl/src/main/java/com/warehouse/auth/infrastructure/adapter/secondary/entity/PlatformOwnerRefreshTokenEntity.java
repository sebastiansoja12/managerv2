package com.warehouse.auth.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.PlatformOwnerId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "platform_owner_refresh_tokens")
public class PlatformOwnerRefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "platform_owner_id", nullable = false))
    private PlatformOwnerId platformOwnerId;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;
}
