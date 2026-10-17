package com.warehouse.asyncjob.domain.port.secondary;

import com.warehouse.asyncjob.domain.model.AsyncJob;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AsyncJobRepository {

    AsyncJob save(final AsyncJob job);

    Optional<AsyncJob> findById(final UUID id);

    List<AsyncJob> findLatestByOperatorId(final Long operatorId, final int limit);
}
