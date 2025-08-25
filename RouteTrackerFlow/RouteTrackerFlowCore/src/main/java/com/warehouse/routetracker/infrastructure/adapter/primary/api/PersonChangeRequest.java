package com.warehouse.routetracker.infrastructure.adapter.primary.api;

import com.warehouse.commonassets.identificator.ShipmentId;

public record PersonChangeRequest(
		ShipmentId shipmentId,
		String firstName, String lastName, String email, String telephoneNumber, String city,
		String postalCode, String street) {
}
