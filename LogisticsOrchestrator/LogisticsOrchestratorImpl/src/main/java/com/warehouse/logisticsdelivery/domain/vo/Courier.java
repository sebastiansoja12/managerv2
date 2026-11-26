package com.warehouse.logisticsdelivery.domain.vo;

import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.VehicleId;

import java.util.Objects;
import java.util.Set;

public record Courier(SupplierId supplierId, VehicleId vehicleId, Set<String> postalCodes,
                      Integer maxActiveDeliveries) {

    public Courier {
        Objects.requireNonNull(supplierId, "Supplier id must not be null");
        postalCodes = postalCodes == null ? Set.of() : Set.copyOf(postalCodes);
    }

    public boolean covers(final String postalCode) {
        return this.postalCodes.contains(postalCode);
    }

    public boolean canTake(final long activeDeliveries) {
        return this.maxActiveDeliveries == null || this.maxActiveDeliveries <= 0
                || activeDeliveries < this.maxActiveDeliveries;
    }
}
