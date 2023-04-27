package com.warehouse.star.infrastructure.adapter.primary.mapper;

import com.warehouse.depot.api.dto.CoordinatesDto;
import com.warehouse.depot.api.dto.DepotDto;
import com.warehouse.star.domain.model.Coordinates;
import com.warehouse.star.domain.model.Depot;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface StarRequestMapper {

    List<Depot> map(List<DepotDto> depots);

    Depot map(DepotDto depot);

    Coordinates map(CoordinatesDto coordinates);
}
