package com.warehouse.commonassets.identificator;

import java.util.Objects;

public class DeliveryId {

	private String id;

	public DeliveryId(String id) {
		this.id = id;
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
