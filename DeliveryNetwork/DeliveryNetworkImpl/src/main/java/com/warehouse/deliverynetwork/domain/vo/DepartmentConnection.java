package com.warehouse.deliverynetwork.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.deliverynetwork.domain.exception.SelfDepartmentConnectionException;

import java.util.Objects;

public record DepartmentConnection(DepartmentId firstDepartmentId, DepartmentId secondDepartmentId,
                                   boolean bidirectional) {

    public DepartmentConnection(final DepartmentId firstDepartmentId, final DepartmentId secondDepartmentId) {
        this(firstDepartmentId, secondDepartmentId, true);
    }

    public DepartmentConnection {
        Objects.requireNonNull(firstDepartmentId, "First department ID cannot be null");
        Objects.requireNonNull(secondDepartmentId, "Second department ID cannot be null");
        Objects.requireNonNull(firstDepartmentId.getValue(), "First department ID value cannot be null");
        Objects.requireNonNull(secondDepartmentId.getValue(), "Second department ID value cannot be null");

        if (firstDepartmentId.equals(secondDepartmentId)) {
            throw new SelfDepartmentConnectionException(firstDepartmentId);
        }

        if (bidirectional && firstDepartmentId.getValue() > secondDepartmentId.getValue()) {
            final DepartmentId originalFirstDepartmentId = firstDepartmentId;
            firstDepartmentId = secondDepartmentId;
            secondDepartmentId = originalFirstDepartmentId;
        }
    }

    public static DepartmentConnection oneWay(final DepartmentId sourceDepartmentId,
                                              final DepartmentId targetDepartmentId) {
        return new DepartmentConnection(sourceDepartmentId, targetDepartmentId, false);
    }

    public boolean connects(final DepartmentId departmentId) {
        return this.firstDepartmentId.equals(departmentId) || this.secondDepartmentId.equals(departmentId);
    }

    public boolean allowsTravel(final DepartmentId sourceDepartmentId, final DepartmentId targetDepartmentId) {
        if (this.firstDepartmentId.equals(sourceDepartmentId) && this.secondDepartmentId.equals(targetDepartmentId)) {
            return true;
        }
        return this.bidirectional
                && this.secondDepartmentId.equals(sourceDepartmentId)
                && this.firstDepartmentId.equals(targetDepartmentId);
    }
}
