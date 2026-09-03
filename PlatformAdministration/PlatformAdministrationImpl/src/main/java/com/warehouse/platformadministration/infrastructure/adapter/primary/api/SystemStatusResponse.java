package com.warehouse.platformadministration.infrastructure.adapter.primary.api;

import java.time.Instant;
import java.util.List;

public record SystemStatusResponse(Runtime runtime,
                                   Database database,
                                   Migrations migrations,
                                   MaintenanceModeResponse maintenance) {

    public record Runtime(String applicationName,
                          String version,
                          String javaVersion,
                          Instant startedAt,
                          long uptimeSeconds,
                          List<String> activeProfiles,
                          long usedMemoryMegabytes,
                          long maxMemoryMegabytes,
                          int availableProcessors) {
    }

    public record Database(boolean available, String product, String version, long responseTimeMillis) {
    }

    public record Migrations(long executedChangeSets, String lastChangeSetId, String lastAuthor, Instant lastExecutedAt) {
    }
}
