package com.warehouse.logistics.domain.model;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DeliveryStepId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.logistics.domain.enumeration.DeliveryMethod;
import com.warehouse.logistics.domain.enumeration.DeliveryStatus;
import com.warehouse.logistics.domain.enumeration.DeliveryStepOutcome;

import java.time.LocalDateTime;

public record DeliveryStep(DeliveryStepId id,
                           DeliveryId deliveryId,
                           int stepNumber,
                           LocalDateTime attemptedAt,
                           DeliveryStepOutcome outcome,
                           DeliveryStatus deliveryStatus,
                           ShipmentId signatureId,
                           UserId userId,
                           SupplierId supplierId,
                           DepartmentId departmentId,
                           VehicleId vehicleId,
                           DeliveryMethod method,
                           String comment,
                           String failureReason,
                           String token) {

    public static DeliveryStep created(final DeliveryId deliveryId,
                                       final LocalDateTime createdAt,
                                       final ShipmentId signatureId,
                                       final DeliveryMethod method,
                                       final String comment,
                                       final UserId userId) {
        return new DeliveryStep(DeliveryStepId.generate(), deliveryId, 1, createdAt,
                DeliveryStepOutcome.IN_PROGRESS, DeliveryStatus.DEPOT, signatureId,
                userId, null, null, null, method, comment, null, null);
    }

    public static DeliveryStep attempt(final DeliveryId deliveryId,
                                       final int stepNumber,
                                       final LocalDateTime attemptedAt,
                                       final DeliveryStatus deliveryStatus,
                                       final UserId userId,
                                       final SupplierId supplierId,
                                       final DepartmentId departmentId,
                                       final VehicleId vehicleId,
                                       final DeliveryMethod method,
                                       final String comment,
                                       final String token) {
        final DeliveryStepOutcome outcome = outcome(deliveryStatus);
        final String failureReason = outcome == DeliveryStepOutcome.FAILED ? comment : null;
        return new DeliveryStep(DeliveryStepId.generate(), deliveryId, stepNumber, attemptedAt, outcome,
                deliveryStatus, null, userId, supplierId, departmentId, vehicleId, method, comment, failureReason, token);
    }

    private static DeliveryStepOutcome outcome(final DeliveryStatus deliveryStatus) {
        if (deliveryStatus == DeliveryStatus.DELIVERED) {
            return DeliveryStepOutcome.SUCCEEDED;
        }
        if (deliveryStatus == DeliveryStatus.REJECTED || deliveryStatus == DeliveryStatus.UNAVAILABLE
                || deliveryStatus == DeliveryStatus.LOST) {
            return DeliveryStepOutcome.FAILED;
        }
        return DeliveryStepOutcome.IN_PROGRESS;
    }
}
