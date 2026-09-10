package com.warehouse.asyncjob.domain.service;

import java.util.UUID;

public class AsyncJobNotFoundException extends RuntimeException {

    public AsyncJobNotFoundException(final UUID jobId) {
        super("Async job was not found: " + jobId);
    }
}
