package com.warehouse.shipment.infrastructure.adapter.secondary.api;

import java.time.Instant;

public record ReturnPackageApi(
        ReturnIdDto returnPackageId,
        ShipmentIdDto shipmentId,
        String reason,
        String returnStatus,
        ReturnTokenApi returnToken,
        DepartmentCodeApi assignedDepartmentCode,
        DepartmentCodeApi returnedDepartmentCode,
        UserIdApi assignedTo,
        UserIdApi processedBy,
        ReasonCodeApi reasonCode,
        Long operatorId,
        Instant createdAt,
        Instant updatedAt) {
}
