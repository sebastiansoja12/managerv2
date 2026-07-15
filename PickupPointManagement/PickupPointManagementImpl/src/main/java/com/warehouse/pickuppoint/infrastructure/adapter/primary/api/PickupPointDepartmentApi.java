package com.warehouse.pickuppoint.infrastructure.adapter.primary.api;

import com.warehouse.pickuppoint.api.dto.DepartmentIdDto;

public record PickupPointDepartmentApi(DepartmentIdDto departmentId, String code) {
}
