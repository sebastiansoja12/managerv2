package com.warehouse.platformadministration.infrastructure.adapter.secondary;

import com.warehouse.asyncjob.api.ReadModelRebuildApiService;
import com.warehouse.asyncjob.api.dto.ReadModelRebuildJobDto;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.application.port.primary.command.StartReadModelRebuildCommand;
import com.warehouse.platformadministration.application.port.secondary.ReadModelRebuildServicePort;
import com.warehouse.platformadministration.domain.vo.ReadModelRebuildJob;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ReadModelRebuildServiceAdapter implements ReadModelRebuildServicePort {

    private final ReadModelRebuildApiService readModelRebuildApiService;

    public ReadModelRebuildServiceAdapter(final ReadModelRebuildApiService readModelRebuildApiService) {
        this.readModelRebuildApiService = readModelRebuildApiService;
    }

    @Override
    public List<String> readModelTypes() {
        return readModelRebuildApiService.readModelTypes();
    }

    @Override
    public ReadModelRebuildJob start(final OperatorId operatorId, final StartReadModelRebuildCommand command) {
        return toModel(readModelRebuildApiService.start(command.readModelType(), operatorId.value(),
                command.dateFrom(), command.dateTo()));
    }

    @Override
    public Optional<ReadModelRebuildJob> find(final UUID jobId) {
        return readModelRebuildApiService.find(jobId).map(this::toModel);
    }

    @Override
    public List<ReadModelRebuildJob> findLatest(final OperatorId operatorId, final int limit) {
        return readModelRebuildApiService.findLatest(operatorId.value(), limit)
                .stream()
                .map(this::toModel)
                .toList();
    }

    private ReadModelRebuildJob toModel(final ReadModelRebuildJobDto job) {
        return new ReadModelRebuildJob(job.jobId(), job.readModelType(), job.status(), job.dateFrom(), job.dateTo(),
                job.operatorId() != null ? OperatorId.of(job.operatorId()) : null, job.createdAt(), job.startedAt(),
                job.finishedAt(), job.error());
    }
}
