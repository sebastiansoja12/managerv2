package com.warehouse.deliverynetwork.application.port.primary.command;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.deliverynetwork.domain.enumeration.DepartmentStatus;
import com.warehouse.deliverynetwork.domain.enumeration.DepartmentType;

import java.util.Objects;

public record DepartmentImportCommand(
        DepartmentCode departmentCode,
        DepartmentType departmentType,
        DepartmentStatus status) {

    public DepartmentImportCommand {
        Objects.requireNonNull(departmentCode, "Department code cannot be null");
        Objects.requireNonNull(departmentCode.getValue(), "Department code value cannot be null");
        Objects.requireNonNull(departmentType, "Department type cannot be null");
        Objects.requireNonNull(status, "Department status cannot be null");
    }
}
