package com.warehouse.depot.infrastructure.primary.mapper;

import com.warehouse.depot.api.dto.DepotCodeDto;
import com.warehouse.depot.api.dto.DepotDto;
import com.warehouse.depot.api.dto.DepotIdDto;
import com.warehouse.depot.domain.model.Depot;
import com.warehouse.depot.domain.model.DepotCode;
import com.warehouse.depot.domain.model.DepotId;
import org.mapstruct.Mapper;

@Mapper
public interface DepotRequestMapper {

    Depot map(DepotDto depot);

    DepotId map(DepotIdDto depotIdDto);

    DepotCode map(DepotCodeDto depotCodeDto);
}
