package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.UUID;

@Embeddable
public class SignatureId {

    private Long value;

    protected SignatureId() {
    }

    public SignatureId(final Long value) {
        this.value = value;
    }

    public static SignatureId nextId() {
        return new SignatureId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof SignatureId other)) {
            return false;
        }
        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
