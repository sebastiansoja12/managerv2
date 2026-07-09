package com.warehouse.routetracker.infrastructure.adapter.primary.api;

import com.warehouse.commonassets.identificator.ShipmentId;

public record PersonChangeRequest(
		ShipmentId shipmentId,
		Person person) {
}
