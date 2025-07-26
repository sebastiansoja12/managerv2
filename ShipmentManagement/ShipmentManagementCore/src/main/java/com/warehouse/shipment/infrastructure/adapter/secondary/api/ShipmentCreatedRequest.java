package com.warehouse.shipment.infrastructure.adapter.secondary.api;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

public record ShipmentCreatedRequest(ShipmentId shipmentId) {
    public static ShipmentCreatedRequest from(final ShipmentSnapshot snapshot) {
        return null;
    }
}
