package com.warehouse.logisticsdelivery.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentId;

public record DepartmentCapacity(DepartmentId departmentId, boolean active, Double latitude, Double longitude,
                                 int dailyCapacity, int warningThresholdPercent,
                                 boolean redirectOverflowToNeighbour) {

    public boolean hasLocation() {
        return latitude != null && longitude != null;
    }
}
