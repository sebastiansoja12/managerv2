package com.warehouse.auth.domain.model;

import com.warehouse.auth.domain.vo.RolePermissionId;
import com.warehouse.commonassets.enumeration.UserPermission;

public class RolePermission {
    private RolePermissionId rolePermissionId;
    private UserPermission userPermission;

    public RolePermission() {
    }

    public RolePermission(final UserPermission userPermission) {
        this.userPermission = userPermission;
    }

    public RolePermission(final RolePermissionId rolePermissionId, final UserPermission userPermission) {
        this.userPermission = userPermission;
        this.rolePermissionId = rolePermissionId;
    }

    public UserPermission getPermission() {
        return userPermission;
    }

    public void setPermission(final UserPermission userPermission) {
        this.userPermission = userPermission;
    }

    public RolePermissionId getRolePermissionId() {
        return rolePermissionId;
    }

    public void setRolePermissionId(final RolePermissionId rolePermissionId) {
        this.rolePermissionId = rolePermissionId;
    }

    public boolean isAdmin() {
        return userPermission != null && userPermission.name().contains("ADMIN");
    }

    public boolean isSupplier() {
        return userPermission != null && userPermission.name().contains("SUPPLIER");
    }

    public boolean isManager() {
        return userPermission != null && userPermission.name().contains("MANAGER");
    }
}
