package com.warehouse.auth.infrastructure.adapter.secondary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.warehouse.auth.domain.model.Depot;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.DepotEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface DepotEntityMapper {

    Depot map(DepotEntity depot);
}
