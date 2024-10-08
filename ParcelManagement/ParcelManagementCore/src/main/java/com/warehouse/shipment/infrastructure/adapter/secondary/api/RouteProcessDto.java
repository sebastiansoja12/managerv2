package com.warehouse.shipment.infrastructure.adapter.secondary.api;

import com.warehouse.shipment.infrastructure.api.dto.ShipmentIdDto;

import java.util.UUID;


public class RouteProcessDto {
	private final ShipmentIdDto parcelId;
	private final UUID processId;

	public RouteProcessDto(final ShipmentIdDto parcelId, final UUID processId) {
		this.parcelId = parcelId;
		this.processId = processId;
	}

	public ShipmentIdDto getParcelId() {
		return parcelId;
	}

	public UUID getProcessId() {
		return processId;
	}
}
