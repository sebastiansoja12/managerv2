package com.warehouse.star.infrastructure.adapter.primary.mapper;


import com.warehouse.dto.CoordinatesDto;
import com.warehouse.dto.DepotDto;
import com.warehouse.star.domain.model.Coordinates;
import com.warehouse.star.domain.model.Depot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface StarRequestMapper {

    List<Depot> map(List<DepotDto> depots);

    @Mapping(target = "coordinates", ignore = true)
    Depot map(DepotDto depot);

    Coordinates map(CoordinatesDto coordinates);
}
