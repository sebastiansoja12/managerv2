package com.warehouse.voronoi.infrastructure.adapter.secondary;

import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.port.primary.DepartmentPort;
import com.warehouse.voronoi.domain.model.Depot;
import com.warehouse.voronoi.domain.port.secondary.DepotServicePort;
import com.warehouse.voronoi.infrastructure.adapter.secondary.mapper.DepotResponseMapper;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class DepotServiceAdapter implements DepotServicePort {

    private final DepartmentPort departmentPort;

    private final DepotResponseMapper depotResponseMapper;

    @Override
    public List<Depot> downloadDepots() {
        final List<Department> departments = departmentPort.findAll();
        return depotResponseMapper.map(departments);
    }
}
