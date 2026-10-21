package com.warehouse.asyncjob.domain.exception;

import com.warehouse.asyncjob.domain.vo.AsyncJobId;

public class AsyncJobNotFoundException extends RuntimeException {

    public AsyncJobNotFoundException(final AsyncJobId jobId) {
        super("Async job was not found: " + jobId.value());
    }
}
