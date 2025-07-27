package com.warehouse.routetracker.infrastructure.adapter.primary.api;

public record ShipmentCreatedRequest(ShipmentId shipmentId, UserId createdBy) {
}
