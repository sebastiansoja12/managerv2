package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Locale;

@Embeddable
public record PalletTrackingNumber(String value) implements ObjectValue<String>, Serializable {

    public PalletTrackingNumber {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Pallet tracking number cannot be blank");
        }
        value = value.trim().toUpperCase(Locale.ROOT);
    }
}
