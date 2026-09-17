package com.warehouse.deliverynetwork.domain.vo;

import com.warehouse.deliverynetwork.domain.enumeration.HandlingOperationType;

public record HandlingOperation(
        int sequence,
        DepartmentNode department,
        HandlingOperationType operationType,
        DepartmentNode counterpartDepartment) {
}
