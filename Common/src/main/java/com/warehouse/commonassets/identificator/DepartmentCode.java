package com.warehouse.commonassets.identificator;

import java.util.Objects;

public class DepartmentCode {

	private final String value;

	public DepartmentCode(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final DepartmentCode that = (DepartmentCode) o;
		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}
}
