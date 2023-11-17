package com.warehouse.auth.infrastructure.adapter.primary.mapper;

import com.warehouse.auth.domain.vo.AuthenticationResponse;
import com.warehouse.auth.domain.vo.RegisterResponse;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.infrastructure.adapter.primary.dto.AuthenticationResponseDto;
import com.warehouse.auth.infrastructure.adapter.primary.dto.RegisterResponseDto;
import com.warehouse.auth.infrastructure.adapter.primary.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper
public interface AuthenticationResponseMapper {
    AuthenticationResponseDto map(AuthenticationResponse response);

    RegisterResponseDto map(RegisterResponse response);

    UserDto map(User user);
}
