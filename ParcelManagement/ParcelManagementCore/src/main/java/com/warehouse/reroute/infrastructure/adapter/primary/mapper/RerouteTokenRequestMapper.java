package com.warehouse.reroute.infrastructure.adapter.primary.mapper;

import com.warehouse.reroute.domain.model.Parcel;
import com.warehouse.reroute.domain.model.RerouteRequest;
import com.warehouse.reroute.domain.model.RerouteParcelRequest;
import com.warehouse.reroute.domain.model.Token;
import com.warehouse.reroute.domain.vo.ParcelId;
import com.warehouse.reroute.infrastructure.api.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface RerouteTokenRequestMapper {


    @Mapping(source = "parcelId.value", target = "parcelId")
    @Mapping(source = "email.value", target = "email")
    RerouteRequest map(RerouteRequestDto request);

    @Mapping(source = "parcelId.value", target = "id")
    @Mapping(source = "parcel", target = "parcel")
    @Mapping(source = "token.value", target = "token")
    RerouteParcelRequest map(RerouteParcelRequestDto updateParcelRequestDto);

    Parcel map(ParcelDto parcelDto);

    Token map(TokenDto tokenDto);

    @Mapping(source = "value", target = "value")
    ParcelId map(ParcelIdDto parcelIdDto);
}
