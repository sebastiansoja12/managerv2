package com.warehouse.logisticsdelivery.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentId;

public record DepartmentLoad(DepartmentId departmentId, long deliveriesToday, int dailyCapacity,
                             int utilizationPercent, boolean warning, boolean overloaded) {
}
