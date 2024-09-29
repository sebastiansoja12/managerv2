package com.warehouse.shipment.infrastructure.api.dto;

public class ParcelIdDto {
	private final Long id;

	public ParcelIdDto(final Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
}
