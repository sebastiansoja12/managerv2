package com.warehouse.deliverynetwork.application.port.primary.command;

import com.warehouse.commonassets.identificator.DepartmentCode;

import java.util.Objects;

public record DepartmentConnectionCodeCommand(
        DepartmentCode firstDepartmentCode,
        DepartmentCode secondDepartmentCode) {

    public DepartmentConnectionCodeCommand {
        Objects.requireNonNull(firstDepartmentCode, "First department code cannot be null");
        Objects.requireNonNull(secondDepartmentCode, "Second department code cannot be null");
        Objects.requireNonNull(firstDepartmentCode.getValue(), "First department code value cannot be null");
        Objects.requireNonNull(secondDepartmentCode.getValue(), "Second department code value cannot be null");
    }
}
