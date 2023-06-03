package com.warehouse.depot.infrastructure.primary.mapper;

import com.warehouse.depot.api.dto.DepotDto;
import com.warehouse.depot.domain.model.Depot;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface DepotResponseMapper {

    DepotDto map(Depot depot);

    List<DepotDto> map(List<Depot> depots);
}
