package com.warehouse.auth.infrastructure.adapter.secondary.mapper;

import com.warehouse.auth.domain.vo.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface UserMapper {

    UserEntity map(User user);

    User map(UserEntity userEntity);


    @Mapping(target = "nonExpired", source = "accountNonExpired")
    @Mapping(target = "nonLocked", source = "accountNonLocked")
    UserResponse mapToUserResponse(UserEntity userEntity);
}
