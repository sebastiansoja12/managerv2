package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public record DepartmentMapId(UUID value) implements ObjectValue<UUID>, Serializable {

    public static DepartmentMapId generate() {
        return new DepartmentMapId(UUID.randomUUID());
    }
}
