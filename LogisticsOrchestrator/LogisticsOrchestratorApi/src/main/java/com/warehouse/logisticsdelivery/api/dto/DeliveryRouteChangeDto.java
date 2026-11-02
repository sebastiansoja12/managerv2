package com.warehouse.logisticsdelivery.api.dto;

import com.warehouse.commonassets.identificator.DepartmentId;

public record DeliveryRouteChangeDto(DepartmentId originDepartmentId,
                                     DepartmentId destinationDepartmentId,
                                     String pickupMethod,
                                     String pickupPointId,
                                     String deliveryPickupPointId) {
}
