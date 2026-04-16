package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto;

public class ShipmentIdDto {
    private Long value;

    public ShipmentIdDto() {
    }

    public ShipmentIdDto(final Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }
}
