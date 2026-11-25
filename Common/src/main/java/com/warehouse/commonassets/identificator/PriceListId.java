package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public record PriceListId(Long value) implements Serializable {

    public static PriceListId nextId() {
        return new PriceListId(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
    }
}
