package com.warehouse.shipment.infrastructure.adapter.secondary.api;

import java.time.Instant;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;

public record ReturnPackageApi(
        ReturnIdDto returnPackageId,
        ShipmentIdDto shipmentId,
        String reason,
        String returnStatus,
        ReturnTokenApi returnToken,
        DepartmentId assignedDepartmentId,
        DepartmentId returnedDepartmentId,
        UserIdApi assignedTo,
        UserIdApi processedBy,
        ReasonCodeApi reasonCode,
        OperatorId operatorId,
        Instant createdAt,
        Instant updatedAt) {
}
