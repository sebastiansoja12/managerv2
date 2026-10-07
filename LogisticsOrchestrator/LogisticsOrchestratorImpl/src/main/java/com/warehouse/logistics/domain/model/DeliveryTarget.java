package com.warehouse.logistics.domain.model;

import com.warehouse.commonassets.identificator.ShipmentId;

import java.util.Objects;

/** A reference to the cargo unit that a delivery concerns. */
public record DeliveryTarget(DeliveryTargetType type, String id) {

    public DeliveryTarget {
        Objects.requireNonNull(type, "Delivery target type is required");
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Delivery target id cannot be blank");
        }
    }

    public static DeliveryTarget shipment(final ShipmentId shipmentId) {
        Objects.requireNonNull(shipmentId, "DeliverableShipment id is required");
        return new DeliveryTarget(DeliveryTargetType.SHIPMENT, shipmentId.getValue().toString());
    }

    public static DeliveryTarget pallet(final String palletId) {
        return new DeliveryTarget(DeliveryTargetType.PALLET, palletId);
    }
}
