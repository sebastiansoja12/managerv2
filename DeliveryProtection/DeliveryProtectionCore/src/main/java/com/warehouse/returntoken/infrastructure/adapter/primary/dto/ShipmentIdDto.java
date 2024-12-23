package com.warehouse.returntoken.infrastructure.adapter.primary.dto;

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
