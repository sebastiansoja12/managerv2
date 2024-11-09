package com.warehouse.commonassets.identificator;

import java.util.Objects;

public class SupplierId {

	private final Long value;

	public SupplierId(Long value) {
		this.value = value;
	}

	public Long getValue() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final SupplierId that = (SupplierId) o;
		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}
}
