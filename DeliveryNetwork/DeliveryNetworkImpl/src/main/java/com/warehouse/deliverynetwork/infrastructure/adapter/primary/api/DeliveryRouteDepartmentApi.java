package com.warehouse.deliverynetwork.infrastructure.adapter.primary.api;

public record DeliveryRouteDepartmentApi(
        DepartmentIdApi departmentId,
        String departmentCode,
        String departmentType) {
}
