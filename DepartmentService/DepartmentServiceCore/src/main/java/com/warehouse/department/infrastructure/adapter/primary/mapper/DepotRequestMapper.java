package com.warehouse.department.infrastructure.adapter.primary.mapper;

import com.warehouse.department.domain.model.Depot;
import com.warehouse.department.domain.vo.DepotCode;
import com.warehouse.department.domain.vo.UpdateStreetRequest;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.DepotCodeDto;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.DepotDto;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.UpdateStreetRequestDto;
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
