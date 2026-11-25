package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public record BillingCustomerId(Long value) implements Serializable {

    public static BillingCustomerId nextId() {
        return new BillingCustomerId(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
    }
}
