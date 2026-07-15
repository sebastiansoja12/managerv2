package com.warehouse.deliverynetwork.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.deliverynetwork.domain.exception.SelfDepartmentConnectionException;

import java.util.Objects;

public record DepartmentConnection(DepartmentId firstDepartmentId, DepartmentId secondDepartmentId) {

    public DepartmentConnection {
        Objects.requireNonNull(firstDepartmentId, "First department ID cannot be null");
        Objects.requireNonNull(secondDepartmentId, "Second department ID cannot be null");
        Objects.requireNonNull(firstDepartmentId.getValue(), "First department ID value cannot be null");
        Objects.requireNonNull(secondDepartmentId.getValue(), "Second department ID value cannot be null");

        if (firstDepartmentId.equals(secondDepartmentId)) {
            throw new SelfDepartmentConnectionException(firstDepartmentId);
        }

        if (firstDepartmentId.getValue() > secondDepartmentId.getValue()) {
            final DepartmentId originalFirstDepartmentId = firstDepartmentId;
            firstDepartmentId = secondDepartmentId;
            secondDepartmentId = originalFirstDepartmentId;
        }
    }

    public boolean connects(final DepartmentId departmentId) {
        return this.firstDepartmentId.equals(departmentId) || this.secondDepartmentId.equals(departmentId);
    }
}
