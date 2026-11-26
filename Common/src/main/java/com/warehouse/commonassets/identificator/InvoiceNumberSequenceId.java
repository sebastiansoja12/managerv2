package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public record InvoiceNumberSequenceId(Long value) implements Serializable {

    public static InvoiceNumberSequenceId nextId() {
        return new InvoiceNumberSequenceId(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
    }
}
