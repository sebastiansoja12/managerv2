package com.warehouse.voronoi.domain.service;

import java.util.List;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.voronoi.domain.model.Department;
import com.warehouse.voronoi.domain.model.VoronoiRequest;

public interface ComputeService {

    DepartmentCode calculateDepartmentCode(final VoronoiRequest request, final List<Department> departments);
}
