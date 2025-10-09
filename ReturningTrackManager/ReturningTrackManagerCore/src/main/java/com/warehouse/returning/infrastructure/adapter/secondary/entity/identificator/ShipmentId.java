package com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator;

import jakarta.persistence.Embeddable;

@Embeddable
public class ShipmentId {
    private Long value;
    public ShipmentId() {

    }
    public ShipmentId(final Long value) {
        this.value = value;
    }
    public Long getValue() {
        return value;
    }
}
