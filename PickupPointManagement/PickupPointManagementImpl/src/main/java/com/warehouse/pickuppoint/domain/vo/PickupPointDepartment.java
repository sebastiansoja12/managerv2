package com.warehouse.pickuppoint.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;

import java.util.Objects;

public record PickupPointDepartment(
        DepartmentId departmentId,
        DepartmentCode departmentCode,
        boolean active) {

    public PickupPointDepartment {
        Objects.requireNonNull(departmentId, "Department ID cannot be null");
        Objects.requireNonNull(departmentId.value(), "Department ID value cannot be null");
        Objects.requireNonNull(departmentCode, "Department code cannot be null");
        Objects.requireNonNull(departmentCode.getValue(), "Department code value cannot be null");
    }
}
