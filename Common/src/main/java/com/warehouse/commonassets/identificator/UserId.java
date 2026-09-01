package com.warehouse.commonassets.identificator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public record UserId(Long value) implements Serializable {

    public Long getValue() {
        return value;
    }

    // TODO
    @JsonIgnore
    public boolean isAdmin() {
        return this.value > 1000;
    }
}
