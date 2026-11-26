package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public record InvoiceId(Long value) implements Serializable {

    public static InvoiceId nextId() {
        return new InvoiceId(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
    }
}
