package com.warehouse.voronoi.infrastructure.adapter.secondary;

import java.util.List;

import com.warehouse.department.api.DepartmentApiService;
import com.warehouse.department.api.dto.DepartmentDto;
import com.warehouse.voronoi.domain.model.Coordinates;
import com.warehouse.voronoi.domain.model.Department;
import com.warehouse.voronoi.domain.port.secondary.DepartmentServicePort;


public class DepartmentServiceAdapter implements DepartmentServicePort {

    private final DepartmentApiService departmentApiService;

    public DepartmentServiceAdapter(final DepartmentApiService departmentApiService) {
        this.departmentApiService = departmentApiService;
    }

    @Override
    public List<Department> downloadDepartments() {
        final List<DepartmentDto> departments = departmentApiService.getAllDepartments();
        return departments.stream().map(e -> new Department(e.city(),
                e.street(), e.country(),
                e.departmentCode(), new Coordinates(e.coordinates().latitude(), e.coordinates().longitude()))).toList();
    }
}
