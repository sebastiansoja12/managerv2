package com.warehouse.delivery.domain.model;

public class DeliveryToken {
    private String value;

    public DeliveryToken(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
