package com.warehouse.shipment.infrastructure.adapter.primary.api;

public record SignatureChangeRequestDto(ShipmentIdDto shipmentId, String signature) {
}
