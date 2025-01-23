package com.warehouse.voronoi.infrastructure.adapter.secondary.mapper;

import com.warehouse.voronoi.domain.model.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface DepotResponseMapper {
    List<Department> map(List<com.warehouse.department.domain.model.Department> departments);

    @Mapping(target = "coordinates", ignore = true)
    Department map(com.warehouse.department.domain.model.Department department);
}
