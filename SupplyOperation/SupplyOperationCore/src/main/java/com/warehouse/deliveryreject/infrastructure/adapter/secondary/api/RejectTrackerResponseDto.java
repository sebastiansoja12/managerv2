package com.warehouse.deliveryreject.infrastructure.adapter.secondary.api;

import com.warehouse.delivery.dto.ShipmentIdDto;

public record RejectTrackerResponseDto(ShipmentIdDto shipmentId, ShipmentIdDto newShipmentId, ProcessIdDto processId) {
}
