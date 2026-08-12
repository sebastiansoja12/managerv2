package com.warehouse.commonassets.identificator;

import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class DepartmentId {
    private Long value;

    protected DepartmentId() {
    }

    public DepartmentId(final Long value) {
        this.value = value;
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
