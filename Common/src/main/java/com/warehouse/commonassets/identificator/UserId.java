package com.warehouse.commonassets.identificator;

import java.util.Objects;

public class UserId {

	private final Long value;

	public UserId(final Long value) {
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
		final UserId userId = (UserId) o;
		return Objects.equals(value, userId.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}
}
