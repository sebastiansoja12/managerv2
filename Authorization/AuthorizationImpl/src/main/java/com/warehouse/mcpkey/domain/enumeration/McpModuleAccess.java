package com.warehouse.mcpkey.domain.enumeration;

public enum McpModuleAccess {
    NONE, READ, WRITE;

    public boolean allows(final boolean write) {
        return this == WRITE || (!write && this == READ);
    }
}
