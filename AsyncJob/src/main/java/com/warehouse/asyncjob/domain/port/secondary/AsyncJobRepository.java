package com.warehouse.asyncjob.domain.port.secondary;

import java.util.Optional;
import java.util.UUID;

import com.warehouse.asyncjob.domain.model.AsyncJob;

public interface AsyncJobRepository {

    AsyncJob save(AsyncJob job);

    Optional<AsyncJob> findById(UUID id);
}
