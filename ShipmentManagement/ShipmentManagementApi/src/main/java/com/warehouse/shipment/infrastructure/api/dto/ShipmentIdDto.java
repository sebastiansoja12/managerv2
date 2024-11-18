package com.warehouse.shipment.infrastructure.api.dto;

import com.warehouse.commonassets.identificator.ShipmentId;

import java.util.Objects;

public class ShipmentIdDto {
	private Long value;

	public ShipmentIdDto(final Long value) {
		this.value = value;
	}

	public ShipmentIdDto() {
	}

    public static ShipmentIdDto from(final ShipmentId shipmentId) {
		return new ShipmentIdDto(shipmentId.getValue());
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
