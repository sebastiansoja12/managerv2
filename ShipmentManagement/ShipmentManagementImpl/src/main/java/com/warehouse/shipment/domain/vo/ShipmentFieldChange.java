package com.warehouse.shipment.domain.vo;

import java.util.Objects;

public record ShipmentFieldChange(String field, String previousValue, String currentValue) {

    public boolean changed() {
        return !Objects.equals(this.previousValue, this.currentValue);
    }
}
