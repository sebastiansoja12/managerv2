package com.warehouse.reroute.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.reroute.domain.model.RerouteToken;
import com.warehouse.reroute.domain.vo.RerouteTokenId;
import com.warehouse.reroute.domain.vo.RerouteTokenValue;
import com.warehouse.reroute.infrastructure.adapter.secondary.entity.RerouteTokenEntity;

public class RerouteTokenPersistenceMapper {

    public RerouteTokenEntity toEntity(final RerouteToken rerouteToken) {
        final RerouteTokenEntity entity = new RerouteTokenEntity();
        entity.setId(rerouteToken.getRerouteTokenId() != null ? rerouteToken.getRerouteTokenId().value() : null);
        entity.setToken(rerouteToken.getValue().value());
        entity.setShipmentId(rerouteToken.getShipmentId().getValue());
        entity.setEmail(rerouteToken.getEmail());
        entity.setCreatedAt(rerouteToken.getCreatedAt());
        entity.setExpiresAt(rerouteToken.getExpiresAt());
        entity.setFailedAttempts(rerouteToken.getFailedAttempts());
        entity.setUsedAt(rerouteToken.getUsedAt());
        return entity;
    }

    public RerouteToken toDomain(final RerouteTokenEntity entity) {
        return new RerouteToken(
                new RerouteTokenId(entity.getId()),
                new ShipmentId(entity.getShipmentId()),
                new RerouteTokenValue(entity.getToken()),
                entity.getEmail(),
                entity.getCreatedAt(),
                entity.getExpiresAt(),
                entity.getFailedAttempts() != null ? entity.getFailedAttempts() : 0,
                entity.getUsedAt());
    }
}
