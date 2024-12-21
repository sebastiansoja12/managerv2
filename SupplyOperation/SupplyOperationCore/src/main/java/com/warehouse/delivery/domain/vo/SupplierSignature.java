package com.warehouse.delivery.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;
import lombok.Builder;

import java.util.UUID;

@Builder
public final class SupplierSignature {
    private final UUID deliveryId;
    private final ShipmentId shipmentId;
    private final String token;

    public SupplierSignature(final UUID deliveryId,
                             final ShipmentId shipmentId,
                             final String token) {
        this.deliveryId = deliveryId;
        this.shipmentId = shipmentId;
        this.token = token;
    }

    public UUID getDeliveryId() {
        return deliveryId;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public String getToken() {
        return token;
    }
}
