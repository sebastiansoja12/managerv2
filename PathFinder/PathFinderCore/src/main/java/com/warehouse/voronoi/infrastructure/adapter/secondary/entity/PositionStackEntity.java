package com.warehouse.voronoi.infrastructure.adapter.secondary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "conf_position_stack")
public class PositionStackEntity {
    @Id
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "valid", nullable = false)
    private boolean valid;

    public PositionStackEntity() {
    }

    public PositionStackEntity(final Long id, final String token, final boolean valid) {
        this.id = id;
        this.token = token;
        this.valid = valid;
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public boolean isValid() {
        return valid;
    }
}
