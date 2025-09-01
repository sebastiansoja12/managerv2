package com.warehouse.auth.infrastructure.adapter.secondary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.auth.domain.vo.UserResponse;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;

@Mapper
public interface UserMapper {


    @Mapping(target = "nonExpired", source = "accountNonExpired")
    @Mapping(target = "nonLocked", source = "accountNonLocked")
    UserResponse mapToUserResponse(UserEntity userEntity);
}
