package com.warehouse.returning.application.port.primary.command;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.returning.domain.vo.ReturnPackageId;

import java.util.Objects;
import java.util.UUID;

public record ApplyReturnCancelledCommand(UUID eventId, ShipmentId shipmentId,
                                                  ReturnPackageId returnPackageId, DepartmentId departmentId,
                                                  OperatorId operatorId, UserId userId) {

    public ApplyReturnCancelledCommand {
        Objects.requireNonNull(eventId, "Event ID is required");
        Objects.requireNonNull(shipmentId, "Shipment ID is required");
        Objects.requireNonNull(returnPackageId, "Return package ID is required");
        Objects.requireNonNull(departmentId, "Department ID is required");
        Objects.requireNonNull(operatorId, "Operator ID is required");
    }
}
