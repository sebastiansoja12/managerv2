package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public record DriverId(Long value) implements ObjectValue<Long>, Serializable {

    public static DriverId of(final Long value) {
        return value == null ? null : new DriverId(value);
    }

    public static DriverId generate() {
        return new DriverId(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
    }

    public Long getValue() {
        return value;
    }
}
