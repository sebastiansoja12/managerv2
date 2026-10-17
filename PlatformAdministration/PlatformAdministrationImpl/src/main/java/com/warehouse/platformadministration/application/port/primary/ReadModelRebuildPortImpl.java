package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.application.port.primary.command.StartReadModelRebuildCommand;
import com.warehouse.platformadministration.application.port.secondary.OperatorServicePort;
import com.warehouse.platformadministration.application.port.secondary.ReadModelRebuildServicePort;
import com.warehouse.platformadministration.domain.exception.OperatorNotFoundException;
import com.warehouse.platformadministration.domain.exception.ReadModelRebuildJobNotFoundException;
import com.warehouse.platformadministration.domain.vo.ReadModelRebuildJob;
import lombok.extern.slf4j.Slf4j;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Slf4j
public class ReadModelRebuildPortImpl implements ReadModelRebuildPort {

    static final int MAX_RANGE_DAYS = 366;

    static final int LATEST_JOBS_LIMIT = 20;

    private final OperatorServicePort operatorServicePort;

    private final ReadModelRebuildServicePort readModelRebuildServicePort;

    public ReadModelRebuildPortImpl(final OperatorServicePort operatorServicePort,
                                    final ReadModelRebuildServicePort readModelRebuildServicePort) {
        this.operatorServicePort = operatorServicePort;
        this.readModelRebuildServicePort = readModelRebuildServicePort;
    }

    @Override
    public List<String> readModelTypes() {
        return readModelRebuildServicePort.readModelTypes();
    }

    @Override
    public ReadModelRebuildJob start(final OperatorId operatorId, final StartReadModelRebuildCommand command) {
        requireOperator(operatorId);
        validate(command);
        final ReadModelRebuildJob job = readModelRebuildServicePort.start(operatorId, command);
        log.info("Platform owner started {} read model rebuild {} for operator {}", command.readModelType(),
                job.jobId(), operatorId.value());
        return job;
    }

    @Override
    public ReadModelRebuildJob find(final UUID jobId) {
        return readModelRebuildServicePort.find(jobId)
                .orElseThrow(() -> new ReadModelRebuildJobNotFoundException(jobId));
    }

    @Override
    public List<ReadModelRebuildJob> findLatest(final OperatorId operatorId) {
        requireOperator(operatorId);
        return readModelRebuildServicePort.findLatest(operatorId, LATEST_JOBS_LIMIT);
    }

    private void validate(final StartReadModelRebuildCommand command) {
        if (!readModelRebuildServicePort.readModelTypes().contains(command.readModelType())) {
            throw new IllegalArgumentException("Unknown read model type " + command.readModelType());
        }
        if (command.dateFrom() == null || command.dateTo() == null || command.dateFrom().isAfter(command.dateTo())) {
            throw new IllegalArgumentException("dateFrom must be less than or equal to dateTo");
        }
        if (ChronoUnit.DAYS.between(command.dateFrom(), command.dateTo()) > MAX_RANGE_DAYS) {
            throw new IllegalArgumentException("Read model rebuild range cannot exceed " + MAX_RANGE_DAYS + " days");
        }
    }

    private void requireOperator(final OperatorId operatorId) {
        if (operatorServicePort.findScope(operatorId).isEmpty()) {
            throw new OperatorNotFoundException(operatorId);
        }
    }
}
