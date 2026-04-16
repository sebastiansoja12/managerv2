package com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator;

import jakarta.persistence.Embeddable;

@Embeddable
public class UserId {
    private Long value;

    public UserId() {
    }

    public UserId(final Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }
}
