package com.warehouse.auth.infrastructure.adapter.secondary.mapper;


import java.time.Instant;

import com.warehouse.auth.domain.model.Token;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.auth.domain.model.RefreshToken;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.RefreshTokenEntity;

@Mapper
public interface RefreshTokenMapper {


    RefreshToken map(RefreshTokenEntity refreshToken);

    @Mapping(target = "revoked", ignore = true)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "tokenType", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "expired", ignore = true)
    RefreshTokenEntity map(RefreshToken refreshToken);

    @Mapping(target = "value", source = "token")
    Token mapToToken(RefreshTokenEntity entity);

    default boolean isTokenExpired(RefreshToken refreshToken) {
        return refreshToken.getExpiryDate().isAfter(Instant.now());
    }
}
