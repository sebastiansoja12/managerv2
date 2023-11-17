package com.warehouse.auth.infrastructure.adapter.primary.mapper;

import com.warehouse.auth.domain.vo.LoginRequest;
import com.warehouse.auth.domain.model.RefreshTokenRequest;
import com.warehouse.auth.domain.model.RegisterRequest;
import com.warehouse.auth.infrastructure.adapter.primary.dto.LoginRequestDto;
import com.warehouse.auth.infrastructure.adapter.primary.dto.RefreshTokenRequestDto;
import com.warehouse.auth.infrastructure.adapter.primary.dto.RegisterRequestDto;
import org.mapstruct.Mapper;

@Mapper
public interface AuthenticationRequestMapper {
    LoginRequest map(LoginRequestDto request);

    RegisterRequest map(RegisterRequestDto request);

    RefreshTokenRequest map(RefreshTokenRequestDto request);
}
