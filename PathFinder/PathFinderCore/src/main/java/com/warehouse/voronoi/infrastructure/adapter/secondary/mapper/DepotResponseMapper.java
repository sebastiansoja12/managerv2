package com.warehouse.voronoi.infrastructure.adapter.secondary.mapper;

import com.warehouse.department.domain.model.Department;
import com.warehouse.voronoi.domain.model.Depot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface DepotResponseMapper {
    List<Depot> map(List<Department> departments);

    @Mapping(target = "coordinates", ignore = true)
    Depot map(Department department);
}
