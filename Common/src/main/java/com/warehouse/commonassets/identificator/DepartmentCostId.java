package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public record DepartmentCostId(Long value) implements Serializable {

    public static DepartmentCostId nextId() {
        return new DepartmentCostId(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
    }
}
