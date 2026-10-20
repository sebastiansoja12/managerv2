package com.warehouse.asyncjob.infrastructure.adapter.primary;

import com.warehouse.asyncjob.api.ReadModelRebuildApiService;
import com.warehouse.asyncjob.api.dto.ReadModelRebuildJobDto;
import com.warehouse.asyncjob.application.port.primary.AsyncJobPort;
import com.warehouse.asyncjob.application.port.primary.command.CreateReadModelRebuildJobCommand;
import com.warehouse.asyncjob.domain.enumeration.ReadModelType;
import com.warehouse.asyncjob.domain.exception.AsyncJobNotFoundException;
import com.warehouse.asyncjob.domain.vo.AsyncJobId;
import com.warehouse.asyncjob.infrastructure.adapter.primary.mapper.AsyncJobResponseMapper;
import com.warehouse.commonassets.identificator.OperatorId;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ReadModelRebuildApiServiceAdapter implements ReadModelRebuildApiService {

    private final AsyncJobPort asyncJobPort;
    private final AsyncJobResponseMapper responseMapper;

    public ReadModelRebuildApiServiceAdapter(final AsyncJobPort asyncJobPort,
                                             final AsyncJobResponseMapper responseMapper) {
        this.asyncJobPort = asyncJobPort;
        this.responseMapper = responseMapper;
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
        return this.responseMapper.toDto(this.asyncJobPort.createReadModelRebuildJob(
                new CreateReadModelRebuildJobCommand(ReadModelType.valueOf(readModelType), OperatorId.of(operatorId),
                        dateFrom, dateTo)));
    }

    @Override
    public Optional<ReadModelRebuildJobDto> find(final UUID jobId) {
        try {
            return Optional.of(this.responseMapper.toDto(this.asyncJobPort.get(new AsyncJobId(jobId))));
        } catch (final AsyncJobNotFoundException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<ReadModelRebuildJobDto> findLatest(final Long operatorId, final int limit) {
        return this.asyncJobPort.findLatest(OperatorId.of(operatorId), limit).stream()
                .map(this.responseMapper::toDto)
                .toList();
    }
}
