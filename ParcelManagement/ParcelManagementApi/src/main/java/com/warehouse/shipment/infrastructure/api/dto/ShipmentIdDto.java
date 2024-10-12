package com.warehouse.shipment.infrastructure.api.dto;

import java.util.Objects;

public class ShipmentIdDto {
	private Long value;

	public ShipmentIdDto(final Long value) {
		this.value = value;
	}

	public ShipmentIdDto() {
	}

	public Long getValue() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final ShipmentIdDto that = (ShipmentIdDto) o;
		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}
}
