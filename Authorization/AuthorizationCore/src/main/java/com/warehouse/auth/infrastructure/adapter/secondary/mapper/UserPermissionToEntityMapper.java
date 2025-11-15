package com.warehouse.auth.infrastructure.adapter.secondary.mapper;

import com.warehouse.auth.domain.model.RolePermission;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.RolePermissionEntity;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class UserPermissionToEntityMapper {

    public static Set<RolePermissionEntity> map(final Set<RolePermission> permissions) {
        return permissions.stream().map(permission -> new RolePermissionEntity(permission.getRolePermissionId().value(), permission.getPermission().name()))
                .collect(Collectors.toSet());
    }
}
