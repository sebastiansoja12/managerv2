package com.warehouse.auth.infrastructure.adapter.secondary.mapper;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;

public abstract class UserToEntityMapper {
    public static UserEntity map(final User user) {
        return UserEntity.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .role(UserRoleMapper.map(user.getRole()))
                .departmentCode(user.getDepartmentCode())
                .apiKey(user.getApiKey())
                .permissions(UserPermissionToEntityMapper.map(user.getPermissions()))
                .build();
    }
}
