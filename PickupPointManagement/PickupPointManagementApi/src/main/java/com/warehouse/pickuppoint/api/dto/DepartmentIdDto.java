package com.warehouse.pickuppoint.api.dto;

import java.util.Objects;

public record DepartmentIdDto(Long value) {

    public DepartmentIdDto {
        Objects.requireNonNull(value, "Department ID value cannot be null");
    }
}
