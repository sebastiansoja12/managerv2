package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.depot.api.dto.DepotDto;

import java.util.List;

public interface DepartmentServicePort {
    List<DepotDto> obtainDepartments();
}
