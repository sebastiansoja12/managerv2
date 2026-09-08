package com.warehouse.logisticsdelivery.api.event.snapshot;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.commonassets.identificator.ShipmentId;

import java.time.LocalDateTime;

public record DeliveryEventData(
        DeliveryId deliveryId,
        String targetType,
        String targetId,
        ShipmentId shipmentId,
        String type,
        String deliveryStatus,
        String lifecycleStatus,
        DepartmentId originDepartmentId,
        DepartmentId destinationDepartmentId,
        String pickupMethod,
        String deliveryMethod,
        PickupPointId pickupPointId,
        PickupPointId deliveryPickupPointId,
        Boolean signatureRequired,
        @JsonFormat(shape = JsonFormat.Shape.STRING) LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING) LocalDateTime plannedAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING) LocalDateTime deliveredAt,
        DeliveryStepEventData lastStep
) {
}
