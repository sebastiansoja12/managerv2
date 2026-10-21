package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public record VehicleId(Long value) {

    public static VehicleId generate() {
        return new VehicleId(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
    }
}
