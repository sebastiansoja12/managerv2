package com.warehouse.deliverynetwork.api.dto;

import com.warehouse.commonassets.identificator.DepartmentCode;

public record RouteDepartmentDto(DepartmentIdDto departmentId,
                                 DepartmentCode departmentCode,
                                 String departmentType,
                                 String status) {
}
