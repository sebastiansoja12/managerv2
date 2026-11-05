package com.warehouse.voronoi.domain.service;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.voronoi.domain.model.Department;
import com.warehouse.voronoi.domain.model.VoronoiRequest;

import java.util.List;

public interface ComputeService {

    DepartmentCode calculateNearestDepartment(final VoronoiRequest request, final List<Department> departments);
}
