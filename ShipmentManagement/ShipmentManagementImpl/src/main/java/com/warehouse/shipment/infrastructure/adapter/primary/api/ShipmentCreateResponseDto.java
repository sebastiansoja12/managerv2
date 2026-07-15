package com.warehouse.shipment.infrastructure.adapter.primary.api;

import com.warehouse.shipment.application.port.primary.result.ShipmentCreateResponse;

public record ShipmentCreateResponseDto(String shipmentId, String trackingNumber) {
    public static ShipmentCreateResponseDto from(final ShipmentCreateResponse response) {
        return new ShipmentCreateResponseDto(response.shipmentId().value().toString(),
                response.trackingNumber());
    }
}
