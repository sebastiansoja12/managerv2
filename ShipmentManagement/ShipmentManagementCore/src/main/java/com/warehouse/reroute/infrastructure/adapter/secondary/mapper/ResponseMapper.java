package com.warehouse.reroute.infrastructure.adapter.secondary.mapper;

import com.warehouse.reroute.domain.model.RerouteRequest;
import com.warehouse.reroute.domain.vo.RerouteResponse;
import com.warehouse.reroute.domain.model.RerouteToken;
import com.warehouse.reroute.domain.vo.RerouteTokenResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ResponseMapper {

    @Mapping(source = "valid", target = "valid")
    @Mapping(source = "parcelId", target = "parcelId.value")
    RerouteTokenResponse map(RerouteToken token);

    RerouteResponse map(RerouteRequest request, Integer token);
}
