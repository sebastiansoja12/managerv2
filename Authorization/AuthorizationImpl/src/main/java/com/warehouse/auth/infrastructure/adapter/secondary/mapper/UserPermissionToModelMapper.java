package com.warehouse.auth.infrastructure.adapter.secondary.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import com.warehouse.auth.domain.model.RolePermission;
import com.warehouse.auth.domain.vo.RolePermissionId;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.RolePermissionEntity;
import com.warehouse.commonassets.enumeration.UserPermission;

public abstract class UserPermissionToModelMapper {

	public static Set<RolePermission> map(final Set<RolePermissionEntity> permissions) {
		return permissions.stream().map(permission -> new RolePermission(new RolePermissionId(permission.getId()),
				UserPermission.valueOf(permission.getName()))).collect(Collectors.toSet());
	}
}
