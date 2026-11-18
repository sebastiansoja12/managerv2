package com.warehouse.organisationstructure.api.dto;

public record DepartmentCapacityPolicyDto(
        int defaultDailyCapacity,
        int capacityWarningThresholdPercent,
        boolean redirectOverflowToNeighbour,
        int maxCouriersPerDepartment
) {
}
