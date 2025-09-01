package com.warehouse.auth.infrastructure.adapter.primary.mapper;

import org.mapstruct.Mapper;

import com.warehouse.auth.domain.model.RefreshTokenRequest;
import com.warehouse.auth.infrastructure.adapter.primary.dto.RefreshTokenRequestDto;

@Mapper
public interface AuthenticationRequestMapper {
    RefreshTokenRequest map(RefreshTokenRequestDto request);
}
