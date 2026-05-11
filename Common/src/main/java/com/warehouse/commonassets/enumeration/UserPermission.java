package com.warehouse.commonassets.enumeration;

public enum UserPermission {
    ROLE_ADMIN_READ("admin:read"),
    ROLE_ADMIN_UPDATE("admin:update"),
    ROLE_ADMIN_CREATE("admin:create"),
    ROLE_ADMIN_DELETE("admin:delete"),

    ROLE_MANAGER_READ("management:read"),
    ROLE_MANAGER_UPDATE("management:update"),
    ROLE_MANAGER_CREATE("management:create"),
    ROLE_MANAGER_DELETE("management:delete"),

    ROLE_SUPPLIER_READ("supplier:read"),
    ROLE_SUPPLIER_UPDATE("supplier:update"),
    ROLE_SUPPLIER_CREATE("supplier:create"),
    ROLE_SUPPLIER_DELETE("supplier:delete"),



    ROLE_OPERATOR_CREATE("operator:create");

    private final String permission;

    UserPermission(final String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
