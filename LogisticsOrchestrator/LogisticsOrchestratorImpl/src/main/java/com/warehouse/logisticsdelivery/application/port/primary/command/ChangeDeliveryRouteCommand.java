package com.warehouse.logisticsdelivery.application.port.primary.command;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.logisticsdelivery.domain.enumeration.PickupMethod;

public record ChangeDeliveryRouteCommand(DeliveryId deliveryId,
                                         DepartmentId originDepartmentId,
                                         DepartmentId destinationDepartmentId,
                                         PickupMethod pickupMethod,
                                         PickupPointId pickupPointId,
                                         PickupPointId deliveryPickupPointId) {
}
