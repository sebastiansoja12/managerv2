package com.warehouse.commonassets.identificator;

import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class ShipmentId {

	private Long value;

	public ShipmentId(final Long value) {
		this.value = value;
	}

	public ShipmentId() {

	}

	public Long getValue() {
		return value;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final ShipmentId shipmentId = (ShipmentId) o;
		return Objects.equals(value, shipmentId.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}
}
