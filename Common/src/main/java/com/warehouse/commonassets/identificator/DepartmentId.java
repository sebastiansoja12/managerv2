package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class DepartmentId implements Serializable {
    private Long value;

    protected DepartmentId() {
    }

    public DepartmentId(final Long value) {
        this.value = value;
    }

    public static DepartmentId generate() {
        return new DepartmentId(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof DepartmentId that)) {
            return false;
        }
        return Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }
}
