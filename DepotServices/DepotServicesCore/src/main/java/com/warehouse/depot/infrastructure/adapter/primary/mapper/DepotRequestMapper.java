package com.warehouse.depot.infrastructure.adapter.primary.mapper;

import com.warehouse.depot.domain.model.Depot;
import com.warehouse.depot.domain.vo.DepotCode;
import com.warehouse.depot.domain.vo.UpdateStreetRequest;
import com.warehouse.depot.infrastructure.adapter.primary.api.dto.DepotCodeDto;
import com.warehouse.depot.infrastructure.adapter.primary.api.dto.DepotDto;
import com.warehouse.depot.infrastructure.adapter.primary.api.dto.UpdateStreetRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface DepotRequestMapper {
    
    @Mapping(target = "depotCode", source = "depotCode.value")
    Depot map(DepotDto depot);
    
    List<Depot> map(List<DepotDto> depots);

    DepotCode map(DepotCodeDto code);

    @Mapping(target = "depotCode.value", source = "depotCode")
    UpdateStreetRequest map(UpdateStreetRequestDto updateStreetRequest);
}
