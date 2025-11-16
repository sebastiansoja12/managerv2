package com.warehouse.auth.infrastructure.adapter.secondary.mapper;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;

public abstract class UserRoleMapper {

    public static User.Role map(final UserEntity.Role role) {
        return switch (role) {
            case ADMIN -> User.Role.ADMIN;
            case USER -> User.Role.USER;
            case MANAGER -> User.Role.MANAGER;
            default -> throw new IllegalArgumentException("Unknown role: " + role);
        };
    }

    public static UserEntity.Role map(final User.Role role) {
        return switch (role) {
            case ADMIN -> UserEntity.Role.ADMIN;
            case USER -> UserEntity.Role.USER;
            case MANAGER -> UserEntity.Role.MANAGER;
            default -> throw new IllegalArgumentException("Unknown role: " + role);
        };
    }
}
