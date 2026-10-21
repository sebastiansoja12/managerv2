package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public record PalletMovementId(UUID value) implements ObjectValue<UUID>, Serializable {

    public static PalletMovementId generate() {
        return new PalletMovementId(UUID.randomUUID());
    }
}
