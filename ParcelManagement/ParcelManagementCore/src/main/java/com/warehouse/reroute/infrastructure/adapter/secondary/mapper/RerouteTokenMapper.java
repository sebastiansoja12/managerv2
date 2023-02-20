package com.warehouse.reroute.infrastructure.adapter.secondary.mapper;

import com.warehouse.reroute.domain.model.RerouteToken;
import com.warehouse.reroute.domain.model.UpdateParcelRequest;
import com.warehouse.reroute.domain.vo.RerouteTokenResponse;
import com.warehouse.reroute.infrastructure.adapter.secondary.entity.RerouteTokenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface RerouteTokenMapper {

    RerouteToken map(RerouteTokenEntity entity);

    RerouteTokenEntity map(RerouteToken rerouteToken);

    RerouteTokenEntity map(UpdateParcelRequest parcelRequest);

    @Mapping(source = "token", target = "token")
    @Mapping(source = "parcelId", target = "parcelId.value")
    RerouteTokenResponse mapToResponse(RerouteTokenEntity entity);
}
