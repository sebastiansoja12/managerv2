package com.warehouse.voronoi.infrastructure.adapter.secondary;

import java.util.Collections;
import java.util.List;

import com.warehouse.department.domain.port.primary.DepartmentPort;
import com.warehouse.voronoi.domain.model.Department;
import com.warehouse.voronoi.domain.port.secondary.DepotServicePort;

public class DepotServiceAdapter implements DepotServicePort {

    private final DepartmentPort departmentPort;

    public DepotServiceAdapter(final DepartmentPort departmentPort) {
        this.departmentPort = departmentPort;
    }

    @Override
    public List<Department> downloadDepots() {
        final List<com.warehouse.department.domain.model.Department> departments = departmentPort.findAll();
        return Collections.emptyList();
    }
}
