package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public record OperatorSubscriptionId(UUID value) implements ObjectValue<UUID>, Serializable {

    public static OperatorSubscriptionId generate() {
        return new OperatorSubscriptionId(UUID.randomUUID());
    }
}
