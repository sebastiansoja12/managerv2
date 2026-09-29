package com.warehouse.departmentmap.application.port.primary.result;

import com.warehouse.departmentmap.domain.model.DepartmentMap;
import com.warehouse.departmentmap.domain.model.StorageLocation;
import com.warehouse.departmentmap.domain.vo.DepartmentDirection;

import java.util.List;

public record DepartmentMapView(DepartmentMap departmentMap, List<StorageLocation> locations,
                                List<DepartmentDirection> directions) {
}
