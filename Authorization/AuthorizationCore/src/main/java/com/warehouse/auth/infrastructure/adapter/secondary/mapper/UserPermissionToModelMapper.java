package com.warehouse.auth.infrastructure.adapter.secondary.mapper;

import com.warehouse.auth.domain.model.RolePermission;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.vo.RolePermissionId;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.RolePermissionEntity;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class UserPermissionToModelMapper {

	public static Set<RolePermission> map(final Set<RolePermissionEntity> permissions) {
		return permissions.stream().map(permission -> new RolePermission(new RolePermissionId(permission.getId()),
				User.Permission.valueOf(permission.getName()))).collect(Collectors.toSet());
	}
}
