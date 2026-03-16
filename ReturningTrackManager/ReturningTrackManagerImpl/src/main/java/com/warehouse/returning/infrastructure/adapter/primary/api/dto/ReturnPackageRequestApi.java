package com.warehouse.returning.infrastructure.adapter.primary.api.dto;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record ReturnPackageRequestApi(
        ShipmentIdApi shipmentId,
        String reason,
        DepartmentCodeApi departmentCode,
        UserIdApi userId,
        ReasonCodeApi reasonCode
) {
    @Override
    @NonNull
    public String toString() {
        return "ReturnPackageRequestApi{" +
                "shipmentId=" + shipmentId +
                ", reason='" + reason + '\'' +
                ", departmentCode=" + departmentCode +
                ", userId=" + userId +
                ", reasonCode=" + reasonCode +
                '}';
    }
}