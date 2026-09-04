package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public record PalletPlacementId(UUID value) implements ObjectValue<UUID>, Serializable {

    public static PalletPlacementId generate() {
        return new PalletPlacementId(UUID.randomUUID());
    }
}
