package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public record PaymentId(Long value) implements Serializable {

    public static PaymentId nextId() {
        return new PaymentId(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
    }
}
