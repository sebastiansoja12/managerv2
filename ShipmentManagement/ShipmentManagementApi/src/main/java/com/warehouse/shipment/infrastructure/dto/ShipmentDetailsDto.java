package com.warehouse.shipment.infrastructure.dto;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;

public record ShipmentDetailsDto(ShipmentId shipmentId, ShipmentStatus status, ShipmentId relatedShipmentId) {
}
