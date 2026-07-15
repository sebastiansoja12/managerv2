package com.warehouse.pickuppoint.domain.vo;

import com.warehouse.pickuppoint.domain.enumeration.PickupPointShipmentSize;
import com.warehouse.pickuppoint.domain.exception.InvalidPickupPointConfigurationException;

import java.util.Objects;
import java.util.Set;

public record PickupPointServicePolicy(
        Set<PickupPointShipmentSize> allowedShipmentSizes,
        boolean acceptsDangerousGoods) {

    public PickupPointServicePolicy {
        allowedShipmentSizes = Set.copyOf(
                Objects.requireNonNull(allowedShipmentSizes, "Allowed shipment sizes cannot be null"));
        if (allowedShipmentSizes.isEmpty()) {
            throw new InvalidPickupPointConfigurationException(
                    "Pickup point must support at least one shipment size");
        }
    }

    public boolean accepts(final PickupPointShipmentSize shipmentSize, final boolean dangerousGoods) {
        Objects.requireNonNull(shipmentSize, "Shipment size cannot be null");
        return this.allowedShipmentSizes.contains(shipmentSize)
                && (!dangerousGoods || this.acceptsDangerousGoods);
    }
}
