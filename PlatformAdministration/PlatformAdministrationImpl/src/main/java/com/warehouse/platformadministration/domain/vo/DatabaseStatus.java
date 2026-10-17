package com.warehouse.platformadministration.domain.vo;

public record DatabaseStatus(boolean available, String product, String version, long responseTimeMillis) {

    public static DatabaseStatus unavailable(final long responseTimeMillis) {
        return new DatabaseStatus(false, null, null, responseTimeMillis);
    }
}
