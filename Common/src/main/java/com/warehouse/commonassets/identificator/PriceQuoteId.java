package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public record PriceQuoteId(Long value) implements Serializable {

    public static PriceQuoteId nextId() {
        return new PriceQuoteId(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
    }
}
