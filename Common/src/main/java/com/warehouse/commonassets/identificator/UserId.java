package com.warehouse.commonassets.identificator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Embeddable;

@Embeddable
public record UserId(Long value) {

    public Long getValue() {
        return value;
    }

    // TODO
    @JsonIgnore
    public boolean isAdmin() {
        return this.value > 1000;
    }
}
