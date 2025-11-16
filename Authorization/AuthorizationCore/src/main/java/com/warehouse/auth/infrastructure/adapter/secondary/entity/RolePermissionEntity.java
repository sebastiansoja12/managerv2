package com.warehouse.auth.infrastructure.adapter.secondary.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "role_permissions")
public class RolePermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    public RolePermissionEntity() {
    }

    public RolePermissionEntity(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
