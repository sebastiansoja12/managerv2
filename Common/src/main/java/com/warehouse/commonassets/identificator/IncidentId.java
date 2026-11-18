package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public record IncidentId(Long value) implements Serializable {

    public static IncidentId nextId() {
        return new IncidentId(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
    }
}
