package com.warehouse.deliveryreject.dto;

import java.util.Objects;

public record DepartmentCodeDto(String value) {

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final DepartmentCodeDto that = (DepartmentCodeDto) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
