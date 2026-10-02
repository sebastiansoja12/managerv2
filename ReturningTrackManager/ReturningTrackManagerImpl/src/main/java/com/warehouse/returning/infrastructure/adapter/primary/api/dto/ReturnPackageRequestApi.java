package com.warehouse.returning.infrastructure.adapter.primary.api.dto;

import com.warehouse.returning.domain.vo.DepartmentId;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record ReturnPackageRequestApi(
        ShipmentIdApi shipmentId,
        String reason,
        DepartmentId departmentId,
        UserIdApi userId,
        ReasonCodeApi reasonCode
) {
    @Override
    @NonNull
    public String toString() {
        return "ReturnPackageRequestApi{" +
                "shipmentId=" + shipmentId +
                ", reason='" + reason + '\'' +
                ", departmentId=" + departmentId +
                ", userId=" + userId +
                ", reasonCode=" + reasonCode +
                '}';
    }
}