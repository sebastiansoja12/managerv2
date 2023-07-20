package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.depot.api.DepotService;
import com.warehouse.depot.api.dto.DepotDto;
import com.warehouse.shipment.domain.port.secondary.DepartmentServicePort;
import lombok.AllArgsConstructor;

import java.util.List;

// TODO Voronoi algorithm will be refactored and coordinates fields from depot will be deleted.
@AllArgsConstructor
public class DepartmentAdapter implements DepartmentServicePort {

    private final DepotService depotService;

    @Override
    public List<DepotDto> obtainDepartments() {
        return depotService.findAll();
    }
}
