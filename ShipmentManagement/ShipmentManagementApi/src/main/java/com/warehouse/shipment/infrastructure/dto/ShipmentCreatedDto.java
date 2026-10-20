package com.warehouse.shipment.infrastructure.dto;

import com.warehouse.commonassets.identificator.TrackingNumber;

public record ShipmentCreatedDto(TrackingNumber trackingNumber) {
}
