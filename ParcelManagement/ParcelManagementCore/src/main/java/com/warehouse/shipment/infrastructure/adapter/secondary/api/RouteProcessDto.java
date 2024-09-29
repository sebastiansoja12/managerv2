package com.warehouse.shipment.infrastructure.adapter.secondary.api;

import com.warehouse.shipment.infrastructure.api.dto.ParcelIdDto;

import java.util.UUID;


public class RouteProcessDto {
	private final ParcelIdDto parcelId;
	private final UUID processId;

	public RouteProcessDto(final ParcelIdDto parcelId, final UUID processId) {
		this.parcelId = parcelId;
		this.processId = processId;
	}

	public ParcelIdDto getParcelId() {
		return parcelId;
	}

	public UUID getProcessId() {
		return processId;
	}
}
