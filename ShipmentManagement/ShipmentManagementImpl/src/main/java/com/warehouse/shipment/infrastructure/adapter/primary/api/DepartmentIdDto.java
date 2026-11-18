package com.warehouse.shipment.infrastructure.adapter.primary.api;

import com.warehouse.commonassets.identificator.DepartmentId;

public record DepartmentIdDto(Long value) {

    public static DepartmentIdDto from(final DepartmentId departmentId) {
        return departmentId == null ? null : new DepartmentIdDto(departmentId.value());
    }

    public DepartmentId toDepartmentId() {
        return new DepartmentId(value);
    }
}
