package com.warehouse.mcp.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentId;

public record DeliveryRoute(DepartmentId originDepartmentId,
                            DepartmentId destinationDepartmentId,
                            String pickupMethod,
                            String pickupPointId,
                            String deliveryPickupPointId) {
}
