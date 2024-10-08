package com.warehouse.commonassets.identificator;

import java.util.Objects;

public class ShipmentId {

	private final Long id;

	public ShipmentId(final Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final ShipmentId shipmentId = (ShipmentId) o;
		return Objects.equals(id, shipmentId.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}
