package com.warehouse.auth.infrastructure.adapter.primary.mapper;

import com.warehouse.auth.domain.vo.AuthenticationResponse;
import com.warehouse.auth.domain.vo.RegisterResponse;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.infrastructure.adapter.primary.dto.AuthenticationResponseDto;
import com.warehouse.auth.infrastructure.adapter.primary.dto.RegisterResponseDto;
import com.warehouse.auth.infrastructure.adapter.primary.dto.UserDto;
import com.warehouse.commonassets.identificator.DepartmentCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AuthenticationResponseMapper {
    AuthenticationResponseDto map(AuthenticationResponse response);

    @Mapping(target = "userResponse.departmentCode", source = "userResponse.departmentCode.value")
    RegisterResponseDto map(RegisterResponse response);

    UserDto map(User user);

    default String map(final DepartmentCode departmentCode) {
        return departmentCode.value();
    }
}
