package com.warehouse.deliverynetwork.application.port.primary.command;

import com.warehouse.commonassets.identificator.DepartmentId;

public record DepartmentConnectionCommand(
        DepartmentId firstDepartmentId,
        DepartmentId secondDepartmentId) {
}
