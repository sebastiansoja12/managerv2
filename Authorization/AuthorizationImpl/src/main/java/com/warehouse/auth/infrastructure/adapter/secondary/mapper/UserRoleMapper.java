package com.warehouse.auth.infrastructure.adapter.secondary.mapper;

import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;

public abstract class UserRoleMapper {

    public static OrganizationUser.Role map(final UserEntity.Role role) {
        return switch (role) {
            case ADMIN -> OrganizationUser.Role.ADMIN;
            case USER -> OrganizationUser.Role.USER;
            case MANAGER -> OrganizationUser.Role.MANAGER;
            case SUPPLIER -> OrganizationUser.Role.SUPPLIER;
            case SUPERADMIN -> OrganizationUser.Role.SUPERADMIN;
            default -> throw new IllegalArgumentException("Unknown role: " + role);
        };
    }

    public static UserEntity.Role map(final OrganizationUser.Role role) {
        return switch (role) {
            case ADMIN -> UserEntity.Role.ADMIN;
            case USER -> UserEntity.Role.USER;
            case MANAGER -> UserEntity.Role.MANAGER;
            case SUPPLIER -> UserEntity.Role.SUPPLIER;
            case SUPERADMIN -> UserEntity.Role.SUPERADMIN;
            default -> throw new IllegalArgumentException("Unknown role: " + role);
        };
    }
}
