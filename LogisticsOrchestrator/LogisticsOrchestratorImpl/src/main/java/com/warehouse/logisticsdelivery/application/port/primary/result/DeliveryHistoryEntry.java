package com.warehouse.logisticsdelivery.application.port.primary.result;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryHistoryEventType;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryLifecycleStatus;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryMethod;
import com.warehouse.logisticsdelivery.domain.enumeration.PickupMethod;

import java.time.Instant;
import java.time.LocalDateTime;

public record DeliveryHistoryEntry(DeliveryId deliveryId,
                                   DeliveryHistoryEventType eventType,
                                   Instant occurredAt,
                                   UserId userId,
                                   DeliveryStatus deliveryStatus,
                                   DeliveryLifecycleStatus status,
                                   DepartmentId originDepartmentId,
                                   DepartmentId destinationDepartmentId,
                                   PickupMethod pickupMethod,
                                   DeliveryMethod method,
                                   PickupPointId pickupPointId,
                                   PickupPointId deliveryPickupPointId,
                                   SupplierId supplierId,
                                   VehicleId vehicleId,
                                   Boolean signatureRequired,
                                   String instructions,
                                   LocalDateTime plannedAt,
                                   int stepCount) {
}
