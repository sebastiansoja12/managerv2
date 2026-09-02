package com.warehouse.platformadministration.domain.exception;

import java.util.UUID;

public class ReadModelRebuildJobNotFoundException extends RuntimeException {

    public ReadModelRebuildJobNotFoundException(final UUID jobId) {
        super("Read model rebuild job " + jobId + " was not found");
    }
}
