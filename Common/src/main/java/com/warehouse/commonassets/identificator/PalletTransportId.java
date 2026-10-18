package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public record PalletTransportId(UUID value) implements ObjectValue<UUID>, Serializable {

    public static PalletTransportId generate() {
        return new PalletTransportId(UUID.randomUUID());
    }
}
