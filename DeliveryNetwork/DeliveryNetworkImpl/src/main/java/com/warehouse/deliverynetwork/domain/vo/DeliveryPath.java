package com.warehouse.deliverynetwork.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentId;

import java.util.List;
import java.util.Objects;

public record DeliveryPath(List<DepartmentId> departmentIds) {

    public DeliveryPath {
        Objects.requireNonNull(departmentIds, "Delivery path cannot be null");
        if (departmentIds.isEmpty()) {
            throw new IllegalArgumentException("Delivery path cannot be empty");
        }
        departmentIds = List.copyOf(departmentIds);
    }
}
