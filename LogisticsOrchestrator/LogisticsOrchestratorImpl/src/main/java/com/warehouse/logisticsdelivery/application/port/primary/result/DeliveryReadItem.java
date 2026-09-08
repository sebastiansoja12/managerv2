package com.warehouse.logisticsdelivery.application.port.primary.result;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryLifecycleStatus;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryMethod;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryTargetType;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryType;

import java.time.LocalDateTime;

public record DeliveryReadItem(DeliveryId deliveryId,
                               ShipmentId shipmentId,
                               DeliveryTargetType targetType,
                               String targetId,
                               DeliveryType type,
                               DeliveryStatus deliveryStatus,
                               DeliveryLifecycleStatus status,
                               DepartmentId originDepartmentId,
                               DepartmentId destinationDepartmentId,
                               DeliveryMethod method,
                               int stepCount,
                               LocalDateTime createdAt,
                               LocalDateTime deliveredAt) {
}
