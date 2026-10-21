package com.warehouse.departmentmap.application.port.primary.result;

import com.warehouse.departmentmap.domain.model.DepartmentMap;

public record DepartmentMapSummary(DepartmentMap departmentMap,
                                   int locationCount,
                                   int occupiedLocationCount,
                                   int palletCount) {
}
