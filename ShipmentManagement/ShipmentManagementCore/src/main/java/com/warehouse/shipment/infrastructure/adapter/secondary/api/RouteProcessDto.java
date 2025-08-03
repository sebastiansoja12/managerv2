package com.warehouse.shipment.infrastructure.adapter.secondary.api;

import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentIdDto;

import java.util.UUID;


public class RouteProcessDto {
	private final ShipmentIdDto shipmentId;
	private final UUID processId;

	public RouteProcessDto(final ShipmentIdDto shipmentId, final UUID processId) {
		this.shipmentId = shipmentId;
		this.processId = processId;
	}

	public ShipmentIdDto getShipmentId() {
		return shipmentId;
	}

	public UUID getProcessId() {
		return processId;
	}
}
