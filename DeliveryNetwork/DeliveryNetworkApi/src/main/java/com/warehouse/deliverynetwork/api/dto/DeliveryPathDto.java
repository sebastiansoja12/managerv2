package com.warehouse.deliverynetwork.api.dto;

import java.util.List;
import java.util.Objects;

public record DeliveryPathDto(List<DepartmentIdDto> departmentIds) {

    public DeliveryPathDto {
        Objects.requireNonNull(departmentIds, "Delivery path cannot be null");
        departmentIds = List.copyOf(departmentIds);
    }
}
