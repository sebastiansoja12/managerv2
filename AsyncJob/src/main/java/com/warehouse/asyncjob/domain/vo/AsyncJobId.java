package com.warehouse.asyncjob.domain.vo;

import java.util.Objects;
import java.util.UUID;

public record AsyncJobId(UUID value) {

    public AsyncJobId {
        Objects.requireNonNull(value, "Async job id cannot be null");
    }

    public static AsyncJobId generate() {
        return new AsyncJobId(UUID.randomUUID());
    }
}
