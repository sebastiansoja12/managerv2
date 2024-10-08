package com.warehouse.shipment.infrastructure.api.dto;

import java.util.Objects;

public class ShipmentIdDto {
	private final Long id;

	public ShipmentIdDto(final Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final ShipmentIdDto that = (ShipmentIdDto) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}
