package com.warehouse.commonassets.identificator;

import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import jakarta.persistence.Embeddable;

@Embeddable
public record DepartmentCode(String value) implements Serializable {

	public static DepartmentCode empty() {
		return new DepartmentCode(StringUtils.EMPTY);
	}

	public String getValue() {
		return value;
	}

	@Override
	public boolean equals(final Object o) {
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
