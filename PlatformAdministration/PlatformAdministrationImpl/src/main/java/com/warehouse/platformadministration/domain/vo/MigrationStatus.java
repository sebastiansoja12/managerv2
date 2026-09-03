package com.warehouse.platformadministration.domain.vo;

import java.time.Instant;

public record MigrationStatus(long executedChangeSets, String lastChangeSetId, String lastAuthor, Instant lastExecutedAt) {

    public static MigrationStatus unknown() {
        return new MigrationStatus(0, null, null, null);
    }
}
