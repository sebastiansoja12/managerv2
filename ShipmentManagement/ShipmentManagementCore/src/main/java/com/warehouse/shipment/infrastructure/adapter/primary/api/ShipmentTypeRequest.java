package com.warehouse.shipment.infrastructure.adapter.primary.api;

import com.warehouse.commonassets.identificator.ShipmentId;

public record ShipmentTypeRequest(ShipmentId shipmentId, ShipmentTypeDto shipmentType) {
}
