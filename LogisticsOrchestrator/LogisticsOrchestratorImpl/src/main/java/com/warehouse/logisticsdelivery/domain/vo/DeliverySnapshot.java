package com.warehouse.logisticsdelivery.domain.vo;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryLifecycleStatus;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryMethod;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryType;
import com.warehouse.logisticsdelivery.domain.enumeration.PickupMethod;

import java.time.LocalDateTime;
import java.util.List;

public record DeliverySnapshot(DeliveryId deliveryId,
                               DeliveryTarget target,
                               ShipmentId shipmentId,
                               DeliveryType type,
                               LocalDateTime createdAt,
                               SupplierId supplierId,
                               VehicleId vehicleId,
                               DeliveryStatus deliveryStatus,
                               DeliveryLifecycleStatus status,
                               DepartmentId originDepartmentId,
                               DepartmentId destinationDepartmentId,
                               PickupMethod pickupMethod,
                               DeliveryMethod method,
                               PickupPointId pickupPointId,
                               PickupPointId deliveryPickupPointId,
                               Boolean signatureRequired,
                               String instructions,
                               LocalDateTime plannedAt,
                               LocalDateTime deliveredAt,
                               String token,
                               List<DeliveryStepSnapshot> deliverySteps) {

    public DeliverySnapshot {
        deliverySteps = List.copyOf(deliverySteps);
    }
}
