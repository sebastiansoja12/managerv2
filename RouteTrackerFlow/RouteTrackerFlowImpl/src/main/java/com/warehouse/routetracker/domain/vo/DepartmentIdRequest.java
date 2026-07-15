package com.warehouse.routetracker.domain.vo;

import com.warehouse.routetracker.domain.enumeration.ProcessType;
import com.warehouse.routetracker.domain.vo.identifier.DepartmentId;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;

public record DepartmentIdRequest(DepartmentId departmentId, ShipmentId shipmentId, ProcessType processType) {
}
