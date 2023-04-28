package com.warehouse.tsp.infrastructure.adapter.primary.mapper;

import com.warehouse.depot.api.dto.DepotDto;
import com.warehouse.tsp.domain.model.Depot;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface TSPRequestMapper {

    List<Depot> map(List<DepotDto> depot);

    Depot map(DepotDto depot);
}
