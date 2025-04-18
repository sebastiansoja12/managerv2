package com.warehouse.shipment.infrastructure.adapter.primary.api;

import com.warehouse.shipment.domain.vo.ShipmentCreateResponse;

public record ShipmentCreateResponseDto(String routeProcessId, ShipmentIdDto shipmentId) {
    public static ShipmentCreateResponseDto from(final ShipmentCreateResponse response) {
        final ShipmentIdDto shipmentId = new ShipmentIdDto(response.shipmentId().value());
        return new ShipmentCreateResponseDto(response.routeProcessId(), shipmentId);
    }
}
