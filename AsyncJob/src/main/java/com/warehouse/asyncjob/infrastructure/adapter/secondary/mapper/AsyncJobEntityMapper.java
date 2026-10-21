package com.warehouse.asyncjob.infrastructure.adapter.secondary.mapper;

import com.warehouse.asyncjob.domain.model.AsyncJob;
import com.warehouse.asyncjob.domain.vo.AsyncJobId;
import com.warehouse.asyncjob.domain.vo.AsyncJobSnapshot;
import com.warehouse.asyncjob.infrastructure.adapter.secondary.entity.AsyncJobEntity;
import com.warehouse.commonassets.identificator.OperatorId;

public class AsyncJobEntityMapper {

    public AsyncJobEntity toEntity(final AsyncJob job) {
        final AsyncJobSnapshot snapshot = job.snapshot();
        return new AsyncJobEntity(snapshot.id().value(), snapshot.status(), snapshot.readModelType(),
                snapshot.dateFrom(), snapshot.dateTo(), snapshot.operatorId().getValue(), snapshot.createdAt(),
                snapshot.startedAt(), snapshot.finishedAt(), snapshot.error());
    }

    public AsyncJob toDomain(final AsyncJobEntity entity) {
        return new AsyncJob(new AsyncJobId(entity.getId()), entity.getStatus(), entity.getReadModelType(),
                entity.getDateFrom(), entity.getDateTo(), OperatorId.of(entity.getOperatorId()),
                entity.getCreatedAt(), entity.getStartedAt(), entity.getFinishedAt(), entity.getError());
    }
}
