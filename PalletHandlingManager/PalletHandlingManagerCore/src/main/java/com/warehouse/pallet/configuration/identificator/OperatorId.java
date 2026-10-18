package com.warehouse.pallet.configuration.identificator;

import jakarta.persistence.Embeddable;

@Embeddable
public record OperatorId(Long value) {

    public OperatorId {
        if (value == null) {
            throw new IllegalArgumentException("Operator id cannot be null");
        }
    }
}
