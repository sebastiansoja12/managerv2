package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public record PickupPointId(UUID value) {

    public static PickupPointId generate() {
        return new PickupPointId(UUID.randomUUID());
    }

    public UUID getValue() {
        return value;
    }
}
