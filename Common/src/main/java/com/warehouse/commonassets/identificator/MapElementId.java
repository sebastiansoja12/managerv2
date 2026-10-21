package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public record MapElementId(UUID value) implements ObjectValue<UUID>, Serializable {

    public static MapElementId generate() {
        return new MapElementId(UUID.randomUUID());
    }
}
