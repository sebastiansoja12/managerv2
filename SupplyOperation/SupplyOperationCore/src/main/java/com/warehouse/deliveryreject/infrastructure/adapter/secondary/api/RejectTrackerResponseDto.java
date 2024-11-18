package com.warehouse.deliveryreject.infrastructure.adapter.secondary.api;

import com.warehouse.deliveryreject.dto.ShipmentIdDto;

public record RejectTrackerResponseDto(ShipmentIdDto shipmentId, ProcessIdDto processId) {
}
