package com.warehouse.parcelstate.infrastructure.adapter.primary.mapper;

import org.mapstruct.Mapper;

import com.warehouse.parcelstate.domain.model.RerouteResponse;
import com.warehouse.parcelstate.infrastructure.adapter.primary.dto.RerouteResponseDto;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ParcelStateResponseMapper {

    RerouteResponseDto map(RerouteResponse rerouteResponse);
}
