package com.warehouse.commonassets.identificator;

import java.util.Objects;

public class ShipmentId {

	private final Long value;

	public ShipmentId(final Long value) {
		this.value = value;
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
