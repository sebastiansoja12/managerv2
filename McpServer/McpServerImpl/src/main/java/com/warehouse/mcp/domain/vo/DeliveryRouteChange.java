package com.warehouse.mcp.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;

public record DeliveryRouteChange(DepartmentCode originDepartmentCode,
                                  DepartmentCode destinationDepartmentCode,
                                  String pickupMethod,
                                  String pickupPointId,
                                  String deliveryPickupPointId) {
}
