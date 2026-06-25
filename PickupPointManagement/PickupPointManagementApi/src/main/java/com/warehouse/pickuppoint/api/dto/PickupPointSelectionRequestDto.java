package com.warehouse.pickuppoint.api.dto;

import java.util.Objects;

public record PickupPointSelectionRequestDto(
        PickupPointIdDto pickupPointId,
        PickupPointCapabilityDto requiredCapability,
        PickupPointTypeDto requiredType,
        String countryCode,
        PickupPointShipmentSizeDto shipmentSize,
        boolean dangerousGoods) {

    public PickupPointSelectionRequestDto {
        Objects.requireNonNull(pickupPointId, "Pickup point ID cannot be null");
        Objects.requireNonNull(requiredCapability, "Required capability cannot be null");
        Objects.requireNonNull(requiredType, "Required pickup point type cannot be null");
        Objects.requireNonNull(countryCode, "Country code cannot be null");
        Objects.requireNonNull(shipmentSize, "Shipment size cannot be null");
    }
}
