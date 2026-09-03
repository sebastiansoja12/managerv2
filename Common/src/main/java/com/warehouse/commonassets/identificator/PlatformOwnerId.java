package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public record PlatformOwnerId(Long value) implements Serializable {

    public Long getValue() {
        return value;
    }
}
