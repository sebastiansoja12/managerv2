package com.warehouse.auth.infrastructure.adapter.secondary.authority;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    MANAGER_READ("management:read"),
    MANAGER_UPDATE("management:update"),
    MANAGER_CREATE("management:create"),
    MANAGER_DELETE("management:delete"),

    SUPPLIER_READ("supplier:read"),
    SUPPLIER_UPDATE("supplier:update"),
    SUPPLIER_CREATE("supplier:create"),
    SUPPLIER_DELETE("supplier:delete");

    @Getter
    private final String permission;
}
