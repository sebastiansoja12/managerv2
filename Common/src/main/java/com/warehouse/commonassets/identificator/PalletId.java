package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public record PalletId(String value) implements ObjectValue<String>, Serializable {

    public PalletId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Pallet id cannot be blank");
        }
    }
}
