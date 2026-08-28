package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.UUID;

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

	public static ShipmentId nextId() {
		final long randomUUIDBits = UUID.randomUUID().getLeastSignificantBits();
		return new ShipmentId(Math.abs(randomUUIDBits));
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
