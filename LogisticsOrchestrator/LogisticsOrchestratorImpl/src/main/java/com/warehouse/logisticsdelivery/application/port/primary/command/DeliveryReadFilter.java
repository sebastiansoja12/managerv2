package com.warehouse.logisticsdelivery.application.port.primary.command;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryLifecycleStatus;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryMethod;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryTargetType;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryType;

import java.time.LocalDate;
import java.util.List;

public record DeliveryReadFilter(List<ShipmentId> shipmentIds,
                                 String text,
                                 List<DeliveryLifecycleStatus> lifecycleStatuses,
                                 boolean hideCanceled,
                                 boolean hideCompleted,
                                 List<DeliveryStatus> deliveryStatuses,
                                 DeliveryType type,
                                 DeliveryTargetType targetType,
                                 DeliveryMethod method,
                                 DepartmentId departmentId,
                                 LocalDate createdFrom,
                                 LocalDate createdTo) {

    public DeliveryReadFilter {
        shipmentIds = shipmentIds == null ? List.of() : List.copyOf(shipmentIds);
        lifecycleStatuses = lifecycleStatuses == null ? List.of() : List.copyOf(lifecycleStatuses);
        deliveryStatuses = deliveryStatuses == null ? List.of() : List.copyOf(deliveryStatuses);
        text = text == null || text.isBlank() ? null : text.trim();
    }

    public static DeliveryReadFilter byShipments(final List<ShipmentId> shipmentIds) {
        return new DeliveryReadFilter(shipmentIds, null, List.of(), false, false, List.of(), null, null, null, null,
                null, null);
    }
}
