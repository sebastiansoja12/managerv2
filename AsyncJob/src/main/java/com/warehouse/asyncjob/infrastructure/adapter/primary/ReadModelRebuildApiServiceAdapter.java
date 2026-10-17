package com.warehouse.asyncjob.infrastructure.adapter.primary;

import com.warehouse.asyncjob.api.ReadModelRebuildApiService;
import com.warehouse.asyncjob.api.dto.ReadModelRebuildJobDto;
import com.warehouse.asyncjob.domain.model.AsyncJob;
import com.warehouse.asyncjob.domain.model.ReadModelType;
import com.warehouse.asyncjob.domain.service.AsyncJobNotFoundException;
import com.warehouse.asyncjob.domain.service.AsyncJobService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ReadModelRebuildApiServiceAdapter implements ReadModelRebuildApiService {

    private final AsyncJobService asyncJobService;

    public ReadModelRebuildApiServiceAdapter(final AsyncJobService asyncJobService) {
        this.asyncJobService = asyncJobService;
    }

    @Override
    public List<String> readModelTypes() {
        return Arrays.stream(ReadModelType.values())
                .map(ReadModelType::name)
                .toList();
    }

    @Override
    public ReadModelRebuildJobDto start(final String readModelType,
                                        final Long operatorId,
                                        final LocalDate dateFrom,
                                        final LocalDate dateTo) {
        if (dateFrom == null || dateTo == null || dateFrom.isAfter(dateTo)) {
            throw new IllegalArgumentException("dateFrom must be less than or equal to dateTo");
        }
        final AsyncJob job = asyncJobService.createReadModelRebuildJob(ReadModelType.valueOf(readModelType),
                operatorId, dateFrom, dateTo);
        return toDto(job);
    }

    @Override
    public Optional<ReadModelRebuildJobDto> find(final UUID jobId) {
        try {
            return Optional.of(toDto(asyncJobService.get(jobId)));
        } catch (AsyncJobNotFoundException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<ReadModelRebuildJobDto> findLatest(final Long operatorId, final int limit) {
        return asyncJobService.findLatest(operatorId, limit)
                .stream()
                .map(this::toDto)
                .toList();
    }

    private ReadModelRebuildJobDto toDto(final AsyncJob job) {
        return new ReadModelRebuildJobDto(job.getId(), job.getStatus().name(), job.getReadModelType().name(),
                job.getDateFrom(), job.getDateTo(), job.getOperatorId(), job.getCreatedAt(), job.getStartedAt(),
                job.getFinishedAt(), job.getError());
    }
}
