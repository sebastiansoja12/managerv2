package com.warehouse.shipment.infrastructure.adapter.secondary.api;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;

public record ShipmentCreatedRequest(ShipmentId shipmentId, UserId createdBy) {
}
