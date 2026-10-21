package com.warehouse.asyncjob.application.port.secondary;

import com.warehouse.asyncjob.domain.model.AsyncJob;
import com.warehouse.asyncjob.domain.vo.AsyncJobId;
import com.warehouse.commonassets.identificator.OperatorId;

import java.util.List;
import java.util.Optional;

public interface AsyncJobRepository {

    AsyncJob save(final AsyncJob job);

    Optional<AsyncJob> findById(final AsyncJobId id);

    List<AsyncJob> findLatestByOperatorId(final OperatorId operatorId, final int limit);
}
