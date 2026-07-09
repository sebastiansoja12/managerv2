package com.warehouse.process.infrastructure.adapter.secondary;

import java.util.Optional;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.domain.model.ProcessLog;
import com.warehouse.process.domain.port.secondary.ProcessLogReadModelRepository;
import com.warehouse.process.infrastructure.adapter.secondary.entity.read.ProcessLogReadEntity;
import com.warehouse.process.infrastructure.adapter.secondary.mapper.ProcessLogToModelMapper;
import com.warehouse.process.infrastructure.adapter.secondary.mapper.ProcessLogToReadEntityMapper;

public class ProcessLogReadModelRepositoryImpl implements ProcessLogReadModelRepository {

    private final ProcessLogReadRepository processLogRepository;

    public ProcessLogReadModelRepositoryImpl(final ProcessLogReadRepository processLogRepository) {
        this.processLogRepository = processLogRepository;
    }

    @Override
    public void createProcessLog(final ProcessLog processLog) {
        final ProcessLogReadEntity entity = ProcessLogToReadEntityMapper.map(processLog);

        processLogRepository.save(entity);
    }

    @Override
    public Optional<ProcessLog> findById(final ProcessId processId) {
        return processLogRepository.findById(processId)
                .map(ProcessLogToModelMapper::map);
    }

    @Override
    public void syncProcessLog(final ProcessLog processLog) {
        final ProcessLogReadEntity entity = ProcessLogToReadEntityMapper.map(processLog);
        processLogRepository.save(entity);
    }
}
