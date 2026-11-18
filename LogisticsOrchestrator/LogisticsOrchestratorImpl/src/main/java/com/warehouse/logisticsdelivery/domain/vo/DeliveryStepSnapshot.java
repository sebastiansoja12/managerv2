package com.warehouse.logisticsdelivery.domain.vo;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DeliveryStepId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.SignatureId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryCommentCode;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryStepOutcome;

import java.time.LocalDateTime;

public record DeliveryStepSnapshot(DeliveryStepId id,
                                   DeliveryId deliveryId,
                                   int stepNumber,
                                   LocalDateTime attemptedAt,
                                   DeliveryStepOutcome outcome,
                                   DeliveryStatus deliveryStatus,
                                   SignatureId signatureId,
                                   UserId userId,
                                   SupplierId supplierId,
                                   DepartmentId departmentId,
                                   VehicleId vehicleId,
                                   String comment,
                                   DeliveryCommentCode commentCode,
                                   String failureReason,
                                   String token) {
}
