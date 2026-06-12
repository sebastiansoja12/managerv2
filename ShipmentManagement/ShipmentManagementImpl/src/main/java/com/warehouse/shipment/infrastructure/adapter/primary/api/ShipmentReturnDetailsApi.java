package com.warehouse.shipment.infrastructure.adapter.primary.api;

import java.time.Instant;

import com.warehouse.shipment.domain.enumeration.ReturnStatus;

public record ShipmentReturnDetailsApi(
        LongValueApi returnPackageId,
        ShipmentIdDto shipmentId,
        String reason,
        ReturnStatus returnStatus,
        StringValueApi returnToken,
        DepartmentCodeDto assignedDepartmentCode,
        DepartmentCodeDto returnedDepartmentCode,
        LongValueApi assignedTo,
        LongValueApi processedBy,
        StringValueApi reasonCode,
        Long operatorId,
        Instant createdAt,
        Instant updatedAt) {

    public record LongValueApi(Long value) {
    }

    public record StringValueApi(String value) {
    }
}
