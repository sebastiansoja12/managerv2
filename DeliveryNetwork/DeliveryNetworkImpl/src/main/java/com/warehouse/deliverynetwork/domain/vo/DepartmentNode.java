package com.warehouse.deliverynetwork.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.deliverynetwork.domain.enumeration.DepartmentStatus;
import com.warehouse.deliverynetwork.domain.enumeration.DepartmentType;

import java.util.Objects;

public record DepartmentNode(
        DepartmentId departmentId,
        DepartmentCode departmentCode,
        DepartmentType departmentType,
        DepartmentStatus status) {

    public DepartmentNode {
        Objects.requireNonNull(departmentId, "Department ID cannot be null");
        Objects.requireNonNull(departmentId.getValue(), "Department ID value cannot be null");
        Objects.requireNonNull(departmentCode, "Department code cannot be null");
        Objects.requireNonNull(departmentCode.getValue(), "Department code value cannot be null");
        Objects.requireNonNull(departmentType, "Department type cannot be null");
        Objects.requireNonNull(status, "Department status cannot be null");
    }

    public boolean isSortingFacility() {
        return this.departmentType == DepartmentType.SORTING_FACILITY;
    }

    public boolean participatesInDeliveryNetwork() {
        return this.status.participatesInDeliveryNetwork();
    }
}
