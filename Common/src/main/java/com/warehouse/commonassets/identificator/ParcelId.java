package com.warehouse.commonassets.identificator;

import java.util.Objects;

public class ParcelId {

	private final Long id;

	public ParcelId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ParcelId parcelId = (ParcelId) o;
		return Objects.equals(id, parcelId.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}
