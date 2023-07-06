package com.warehouse.auth.infrastructure.adapter.secondary.mapper;

import com.warehouse.auth.domain.model.Depot;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.DepotEntity;
import com.warehouse.depot.api.dto.DepotDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface DepotEntityMapper {

    Depot map(DepotEntity depot);

    Depot mapToDepotFromDepotDto(DepotDto depotDto);
}
