package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.model.RolePermission;

import java.util.Set;

public interface RolePermissionService {
    RolePermission findByName(final String name);
    Set<RolePermission> findAllAdminPermissions();
}
