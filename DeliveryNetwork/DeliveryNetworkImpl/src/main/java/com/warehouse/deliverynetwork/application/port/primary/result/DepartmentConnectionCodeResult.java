package com.warehouse.deliverynetwork.application.port.primary.result;

import com.warehouse.commonassets.identificator.DepartmentCode;

import java.util.Objects;

public record DepartmentConnectionCodeResult(
        DepartmentCode firstDepartmentCode,
        DepartmentCode secondDepartmentCode) {

    public DepartmentConnectionCodeResult {
        Objects.requireNonNull(firstDepartmentCode, "First department code cannot be null");
        Objects.requireNonNull(secondDepartmentCode, "Second department code cannot be null");
        Objects.requireNonNull(firstDepartmentCode.getValue(), "First department code value cannot be null");
        Objects.requireNonNull(secondDepartmentCode.getValue(), "Second department code value cannot be null");

        if (firstDepartmentCode.getValue().compareTo(secondDepartmentCode.getValue()) > 0) {
            final DepartmentCode originalFirstDepartmentCode = firstDepartmentCode;
            firstDepartmentCode = secondDepartmentCode;
            secondDepartmentCode = originalFirstDepartmentCode;
        }
    }
}
