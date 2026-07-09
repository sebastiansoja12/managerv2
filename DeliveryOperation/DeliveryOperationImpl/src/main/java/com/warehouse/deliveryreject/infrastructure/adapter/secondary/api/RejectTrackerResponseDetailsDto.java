package com.warehouse.deliveryreject.infrastructure.adapter.secondary.api;

import com.warehouse.delivery.dto.ShipmentIdDto;

public record RejectTrackerResponseDetailsDto(ShipmentIdDto shipmentId, ShipmentIdDto newShipmentId, ProcessIdDto processId,
                                              Boolean loggedInTracker) {
}
