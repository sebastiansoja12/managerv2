package com.warehouse.asyncjob.infrastructure.adapter.primary.mapper;

import com.warehouse.asyncjob.api.dto.ReadModelRebuildJobDto;
import com.warehouse.asyncjob.domain.vo.AsyncJobSnapshot;
import com.warehouse.asyncjob.infrastructure.adapter.primary.dto.AsyncJobResponse;

public class AsyncJobResponseMapper {

    public AsyncJobResponse toResponse(final AsyncJobSnapshot job) {
        return new AsyncJobResponse(job.id().value(), job.status(), job.readModelType(), job.dateFrom(),
                job.dateTo(), job.operatorId().getValue(), job.createdAt(), job.startedAt(), job.finishedAt(),
                job.error());
    }

    public ReadModelRebuildJobDto toDto(final AsyncJobSnapshot job) {
        return new ReadModelRebuildJobDto(job.id().value(), job.status().name(), job.readModelType().name(),
                job.dateFrom(), job.dateTo(), job.operatorId().getValue(), job.createdAt(), job.startedAt(),
                job.finishedAt(), job.error());
    }
}
