package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public record TrackingSessionId(Long value) implements Serializable {

    public static TrackingSessionId nextId() {
        return new TrackingSessionId(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
    }
}
