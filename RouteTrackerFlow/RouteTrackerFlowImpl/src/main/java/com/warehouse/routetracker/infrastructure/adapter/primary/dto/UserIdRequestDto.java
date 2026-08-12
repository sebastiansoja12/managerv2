package com.warehouse.routetracker.infrastructure.adapter.primary.dto;

public record UserIdRequestDto(UserIdDto userId, ShipmentIdDto shipmentId, ProcessTypeDto processType) {
}
