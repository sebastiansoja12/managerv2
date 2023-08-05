package com.warehouse.voronoi.infrastructure.adapter.primary.mapper;

import com.warehouse.dto.CoordinatesDto;
import com.warehouse.dto.DepotDto;
import com.warehouse.voronoi.domain.model.Coordinates;
import com.warehouse.voronoi.domain.model.Depot;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface AddressRequestMapper {
    Coordinates map(CoordinatesDto coordinatesDto);

    List<Depot> map(List<DepotDto> depots);

    Depot map(DepotDto depot);
}
