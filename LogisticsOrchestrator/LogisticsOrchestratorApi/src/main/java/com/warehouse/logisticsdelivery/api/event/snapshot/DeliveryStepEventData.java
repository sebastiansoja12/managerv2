package com.warehouse.logisticsdelivery.api.event.snapshot;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.VehicleId;

import java.time.LocalDateTime;

public record DeliveryStepEventData(
        int stepNumber,
        @JsonFormat(shape = JsonFormat.Shape.STRING) LocalDateTime attemptedAt,
        String outcome,
        String deliveryStatus,
        UserId userId,
        SupplierId supplierId,
        DepartmentId departmentId,
        VehicleId vehicleId,
        String comment,
        String failureReason,
        String commentCode
) {
}
