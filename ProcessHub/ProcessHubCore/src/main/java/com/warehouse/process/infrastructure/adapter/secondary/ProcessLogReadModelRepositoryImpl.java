package com.warehouse.process.infrastructure.adapter.secondary;

import com.warehouse.process.domain.model.CommunicationLogDetail;
import com.warehouse.process.domain.model.ProcessLog;
import com.warehouse.process.domain.port.secondary.ProcessLogReadModelRepository;
import com.warehouse.process.infrastructure.adapter.secondary.entity.read.CommunicationLogReadEntity;
import com.warehouse.process.infrastructure.adapter.secondary.entity.read.ProcessLogReadEntity;
import com.warehouse.process.infrastructure.adapter.secondary.mapper.ProcessLogToReadEntityMapper;

public class ProcessLogReadModelRepositoryImpl implements ProcessLogReadModelRepository {

    private final ProcessLogReadRepository processLogRepository;

    private final CommunicationLogReadRepository communicationLogRepository;

    public ProcessLogReadModelRepositoryImpl(final ProcessLogReadRepository processLogRepository,
                                             final CommunicationLogReadRepository communicationLogRepository) {
        this.processLogRepository = processLogRepository;
        this.communicationLogRepository = communicationLogRepository;
    }

    @Override
    public void createProcessLog(final ProcessLog processLog) {
        final ProcessLogReadEntity entity = ProcessLogToReadEntityMapper.map(processLog);

        processLogRepository.save(entity);

        processLog.getCommunicationLogDetails()
                .getCommunicationLogDetails()
                .stream()
                .map(detail -> map(detail, entity))
                .forEach(communicationLogRepository::save);
    }

    private CommunicationLogReadEntity map(final CommunicationLogDetail detail, final ProcessLogReadEntity processLog) {
        return ProcessLogToReadEntityMapper.map(detail, processLog);
    }
}
