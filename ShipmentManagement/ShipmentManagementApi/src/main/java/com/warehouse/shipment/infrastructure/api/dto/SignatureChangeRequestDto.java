package com.warehouse.shipment.infrastructure.api.dto;

public record SignatureChangeRequestDto(ShipmentIdDto shipmentId, String signature) {
}
