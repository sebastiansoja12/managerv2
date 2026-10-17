package com.warehouse.shipment.domain.vo;

public record CustomerReference(String value) {

    public CustomerReference {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("value cannot be null or blank");
        }
    }
}
