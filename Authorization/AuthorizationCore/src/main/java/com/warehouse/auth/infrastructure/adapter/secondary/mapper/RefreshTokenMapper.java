package com.warehouse.auth.infrastructure.adapter.secondary.mapper;


import java.time.Instant;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.auth.domain.model.RefreshToken;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.RefreshTokenEntity;

@Mapper
public interface RefreshTokenMapper {


    RefreshToken map(RefreshTokenEntity refreshToken);

    @Mapping(target = "revoked", ignore = true)
    @Mapping(target = "tokenType", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "expired", expression = "java(isTokenExpired(refreshToken))")
    RefreshTokenEntity map(RefreshToken refreshToken);

    default boolean isTokenExpired(RefreshToken refreshToken) {
        return refreshToken.getExpiryDate().isAfter(Instant.now());
    }
}
