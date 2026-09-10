package com.warehouse.auth.infrastructure.adapter.primary.mapper;

import com.warehouse.auth.domain.vo.RegisterResponse;
import com.warehouse.auth.infrastructure.dto.RegisterResponseDto;
import com.warehouse.commonassets.identificator.DepartmentCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AuthenticationResponseMapper {
    @Mapping(target = "userResponse.departmentCode", source = "userResponse.departmentCode.value")
    RegisterResponseDto map(RegisterResponse response);

    default String map(final DepartmentCode departmentCode) {
        return departmentCode.value();
    }
}
