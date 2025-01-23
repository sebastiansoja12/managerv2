package com.warehouse.voronoi.domain.port.secondary;

import com.warehouse.voronoi.domain.model.Department;

import java.util.List;

public interface DepotServicePort {
    List<Department> downloadDepots();
}
