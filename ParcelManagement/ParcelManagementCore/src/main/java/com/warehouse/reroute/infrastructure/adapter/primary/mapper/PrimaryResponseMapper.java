package com.warehouse.reroute.infrastructure.adapter.primary.mapper;

import com.warehouse.reroute.domain.model.RerouteResponse;
import com.warehouse.reroute.domain.vo.ParcelUpdateResponse;
import com.warehouse.reroute.domain.vo.RerouteTokenResponse;
import com.warehouse.reroute.infrastructure.api.dto.ParcelResponseDto;
import com.warehouse.reroute.infrastructure.api.dto.RerouteResponseDto;
import com.warehouse.reroute.infrastructure.api.dto.RerouteTokenResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PrimaryResponseMapper {

    RerouteResponseDto map(RerouteResponse rerouteResponse);

    RerouteResponse map(RerouteResponseDto rerouteResponse);

    @Mapping(source = "parcelSize", target = "parcelSize")
    ParcelResponseDto map(ParcelUpdateResponse parcelUpdateResponse);

    @Mapping(source = "parcelId.value", target = "parcelId")
    RerouteTokenResponseDto map(RerouteTokenResponse rerouteTokenResponse);

    @Mapping(source = "parcelId", target = "parcelId.value")
    RerouteTokenResponse map(RerouteTokenResponseDto rerouteTokenResponse);


}
