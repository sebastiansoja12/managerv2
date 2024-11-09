package com.warehouse.terminal.infrastructure.adapter.secondary.enumeration;


import static com.warehouse.terminal.infrastructure.adapter.secondary.enumeration.Permission.*;

import java.util.Collections;
import java.util.Set;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {

    USER(Collections.emptySet()),
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE,
                    MANAGER_READ,
                    MANAGER_UPDATE,
                    MANAGER_DELETE,
                    MANAGER_CREATE
            )
    ),
    MANAGER(
            Set.of(
                    MANAGER_READ,
                    MANAGER_UPDATE,
                    MANAGER_DELETE,
                    MANAGER_CREATE
            )
    ),

	SUPPLIER(
            Set.of(
                    SUPPLIER_READ,
                    SUPPLIER_UPDATE,
                    SUPPLIER_DELETE,
                    SUPPLIER_CREATE)
    );

    @Getter
    private final Set<Permission> permissions;
}
