package com.warehouse.voronoi.infrastructure.adapter.secondary.mapper;

import com.warehouse.voronoi.domain.model.Depot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface DepotResponseMapper {
    List<Depot> map(List<com.warehouse.depot.domain.model.Depot> depots);

    @Mapping(target = "coordinates", ignore = true)
    Depot map(com.warehouse.depot.domain.model.Depot depot);
}
