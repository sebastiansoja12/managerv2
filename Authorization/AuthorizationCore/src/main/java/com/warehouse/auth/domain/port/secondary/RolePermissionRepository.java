package com.warehouse.auth.domain.port.secondary;

import com.warehouse.auth.domain.model.RolePermission;

import java.util.Set;

public interface RolePermissionRepository {
    RolePermission findByName(final String name);

    Set<RolePermission> findAllAdminPermissions();

    Set<RolePermission> findAllSupplierPermissions();
}
