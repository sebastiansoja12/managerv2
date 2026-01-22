package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public record ExternalId<T>(T value) {


    public static ExternalId<UUID> randomUUID() {
        return new ExternalId<>(UUID.randomUUID());
    }
}

