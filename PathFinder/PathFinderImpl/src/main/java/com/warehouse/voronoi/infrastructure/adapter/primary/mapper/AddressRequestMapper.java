package com.warehouse.voronoi.infrastructure.adapter.primary.mapper;

import com.warehouse.dto.CoordinatesDto;
import com.warehouse.dto.DepotDto;
import com.warehouse.voronoi.domain.model.Coordinates;
import com.warehouse.voronoi.domain.model.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface AddressRequestMapper {
    Coordinates map(CoordinatesDto coordinatesDto);

    List<Department> map(List<DepotDto> depots);
    @Mapping(target = "coordinates", ignore = true)
    Department map(DepotDto depot);
}
