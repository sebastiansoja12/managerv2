package com.warehouse.depot.infrastructure.adapter.primary.mapper;

import com.warehouse.depot.domain.model.Depot;
import com.warehouse.depot.domain.vo.UpdateStreetResponse;
import com.warehouse.depot.infrastructure.adapter.primary.api.dto.DepotDto;
import com.warehouse.depot.infrastructure.adapter.primary.api.dto.UpdateStreetResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface DepotResponseMapper {

    @Mapping(target = "depotCode.value", source = "depotCode")
    DepotDto map(Depot depot);

    List<DepotDto> map(List<Depot> depotList);

    UpdateStreetResponseDto map(UpdateStreetResponse response);
}
