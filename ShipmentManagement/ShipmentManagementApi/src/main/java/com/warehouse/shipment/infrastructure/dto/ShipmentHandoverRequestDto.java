package com.warehouse.shipment.infrastructure.dto;

import com.warehouse.commonassets.identificator.TrackingNumber;

public record ShipmentHandoverRequestDto(TrackingNumber trackingNumber,
                                         String recipientName,
                                         String signature) {
}
