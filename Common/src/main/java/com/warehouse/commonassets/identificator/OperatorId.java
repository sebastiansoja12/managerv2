package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class OperatorId {

    private Long value;

    public OperatorId() {
    }

    public OperatorId(final Long value) {
        this.value = value;
    }

    public static OperatorId of(final Long value) {
        return new OperatorId(value);
    }

    public Long value() {
        return value;
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OperatorId that)) {
            return false;
        }
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value != null ? value.toString() : null;
    }
}
