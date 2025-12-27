package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

@Embeddable
public record VehicleId(Long value) {
}
