package com.warehouse.asyncjob.api;

import com.warehouse.asyncjob.api.dto.ReadModelRebuildJobDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadModelRebuildApiService {

    List<String> readModelTypes();

    ReadModelRebuildJobDto start(final String readModelType,
                                 final Long operatorId,
                                 final LocalDate dateFrom,
                                 final LocalDate dateTo);

    Optional<ReadModelRebuildJobDto> find(final UUID jobId);

    List<ReadModelRebuildJobDto> findLatest(final Long operatorId, final int limit);
}
