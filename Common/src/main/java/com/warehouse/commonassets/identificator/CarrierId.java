package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public record CarrierId(Long value) implements ObjectValue<Long>, Serializable {

    public static CarrierId of(final Long value) {
        return value == null ? null : new CarrierId(value);
    }

    public static CarrierId generate() {
        return new CarrierId(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
    }

    public Long getValue() {
        return value;
    }
}
