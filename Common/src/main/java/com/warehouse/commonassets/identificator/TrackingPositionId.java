package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public record TrackingPositionId(Long value) implements Serializable {

    public static TrackingPositionId nextId() {
        return new TrackingPositionId(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
    }
}
