package com.warehouse.routetracker.infrastructure.adapter.primary.api;

public record PersonChangeRequest(
		ShipmentId shipmentId,
		Person person) {
}
