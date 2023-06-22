package com.warehouse.auth.infrastructure.adapter.secondary.mapper;


import com.warehouse.auth.domain.model.RefreshToken;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.RefreshTokenEntity;
import org.mapstruct.Mapper;

@Mapper
public interface RefreshTokenMapper {

    RefreshToken map(RefreshTokenEntity refreshToken);
}
