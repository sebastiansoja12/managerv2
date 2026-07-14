package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

@Embeddable
public record UserId(Long value) {

    public Long getValue() {
        return value;
    }

    // TODO
    public boolean isAdmin() {
        return this.value > 1000;
    }
}
