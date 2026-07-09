package com.warehouse.reroute.infrastructure.adapter.secondary.mapper;

import com.warehouse.reroute.domain.model.RerouteToken;
import com.warehouse.reroute.domain.model.RerouteParcelRequest;
import com.warehouse.reroute.domain.vo.RerouteProcessor;
import com.warehouse.reroute.infrastructure.adapter.secondary.entity.RerouteTokenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.Instant;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface RerouteTokenMapper {

    RerouteToken map(RerouteTokenEntity entity);

    RerouteTokenEntity map(RerouteToken rerouteToken);

    RerouteTokenEntity map(RerouteParcelRequest parcelRequest);

    default RerouteTokenEntity map(RerouteProcessor rerouteProcessor) {
        return RerouteTokenEntity.builder()
                .token(rerouteProcessor.getGeneratedToken().getValue())
                .createdDate(Instant.now())
                .expiryDate(Instant.now().plusSeconds(600L))
                .email(rerouteProcessor.getEmail())
                .parcelId(rerouteProcessor.getParcelId())
                .build();
    }
}
