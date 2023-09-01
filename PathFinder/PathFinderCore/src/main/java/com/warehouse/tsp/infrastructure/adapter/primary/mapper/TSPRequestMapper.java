package com.warehouse.tsp.infrastructure.adapter.primary.mapper;

import com.warehouse.dto.DepotDto;
import com.warehouse.tsp.domain.model.Depot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface TSPRequestMapper {

    List<Depot> map(List<DepotDto> depot);

    @Mapping(target = "coordinates", ignore = true)
    Depot map(DepotDto depot);
}
