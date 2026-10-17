package com.warehouse.commonassets.enumeration;

public enum AccountLevel {
    USER,
    MANAGER,
    ADMIN,
    SUPERADMIN,
    PLATFORM_OWNER;

    public boolean isAtLeast(final AccountLevel level) {
        return compareTo(level) >= 0;
    }

    public boolean isHigherThan(final AccountLevel level) {
        return compareTo(level) > 0;
    }
}
