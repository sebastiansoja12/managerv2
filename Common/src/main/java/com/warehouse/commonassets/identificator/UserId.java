package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public record UserId(Long value) {

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
