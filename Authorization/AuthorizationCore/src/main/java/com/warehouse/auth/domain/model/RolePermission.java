package com.warehouse.auth.domain.model;

import com.warehouse.auth.domain.vo.RolePermissionId;

public class RolePermission {
    private RolePermissionId rolePermissionId;
    private User.Permission permission;

    public RolePermission() {
    }

    public RolePermission(final User.Permission permission) {
        this.permission = permission;
    }

    public RolePermission(final RolePermissionId rolePermissionId, final User.Permission permission) {
        this.permission = permission;
        this.rolePermissionId = rolePermissionId;
    }

    public User.Permission getPermission() {
        return permission;
    }

    public void setPermission(final User.Permission permission) {
        this.permission = permission;
    }

    public RolePermissionId getRolePermissionId() {
        return rolePermissionId;
    }

    public void setRolePermissionId(final RolePermissionId rolePermissionId) {
        this.rolePermissionId = rolePermissionId;
    }

    public boolean isAdmin() {
        return permission != null && permission.name().contains("ADMIN");
    }

    public boolean isSupplier() {
        return permission != null && permission.name().contains("SUPPLIER");
    }
}
