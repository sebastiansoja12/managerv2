package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.UUID;

@Embeddable
public class DeliveryId {

	private String id;

	protected DeliveryId() {
	}

	public DeliveryId(String id) {
		this.id = id;
	}

	public static DeliveryId generate() {
		return new DeliveryId(UUID.randomUUID().toString());
	}

	public String getId() {
		return id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final DeliveryId that = (DeliveryId) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}
