package com.warehouse.deliveryreject.infrastructure.adapter.secondary.api;

import com.warehouse.delivery.dto.ShipmentIdDto;

public record RejectTrackerResponseDto(ShipmentIdDto shipmentId, ProcessIdDto processId) {
}
