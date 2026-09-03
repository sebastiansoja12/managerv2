package com.warehouse.auth.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.PlatformOwnerId;
import jakarta.persistence.*;
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
@Table(name = "platform_owners")
public class PlatformOwnerEntity {

    @Id
    @Column(name = "platform_owner_id", unique = true, nullable = false)
    @AttributeOverride(name = "value", column = @Column(name = "platform_owner_id"))
    private PlatformOwnerId platformOwnerId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
