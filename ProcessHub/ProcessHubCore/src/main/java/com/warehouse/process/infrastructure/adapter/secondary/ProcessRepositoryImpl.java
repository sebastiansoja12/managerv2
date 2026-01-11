package com.warehouse.process.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.domain.model.ProcessLog;
import com.warehouse.process.domain.port.secondary.ProcessRepository;
import com.warehouse.process.infrastructure.adapter.secondary.entity.identificator.ProcessLogId;
import com.warehouse.process.infrastructure.adapter.secondary.entity.read.ProcessLogReadEntity;
import com.warehouse.process.infrastructure.adapter.secondary.entity.write.ProcessLogWriteEntity;
import com.warehouse.process.infrastructure.adapter.secondary.mapper.ProcessLogToEntityMapper;
import com.warehouse.process.infrastructure.adapter.secondary.mapper.ProcessLogToModelMapper;

import java.util.Optional;

public class ProcessRepositoryImpl implements ProcessRepository {

    private final ProcessLogReadRepository readRepository;
    private final ProcessLogWriteRepository writeRepository;

    public ProcessRepositoryImpl(final ProcessLogReadRepository readRepository,
                                 final ProcessLogWriteRepository writeRepository) {
        this.readRepository = readRepository;
        this.writeRepository = writeRepository;
    }

    @Override
    public void create(final ProcessLog processLog) {
        validateNotExists(processLog.getProcessId());
        final ProcessLogWriteEntity writeEntity = ProcessLogToEntityMapper.map(processLog);
        this.writeRepository.save(writeEntity);
    }

    @Override
    public void update(final ProcessLog processLog) {
        final ProcessLogWriteEntity writeEntity = ProcessLogToEntityMapper.map(processLog);
        this.writeRepository.save(writeEntity);
    }

    @Override
    public ProcessLog findById(final ProcessId processId) {
        return readRepository.findById(new ProcessLogId(processId.value()))
                .map(ProcessLogToModelMapper::map)
                .orElse(null);
    }

    private void validateNotExists(final ProcessId processId) {
        final Optional<ProcessLogReadEntity> processLogReadEntity =
                this.readRepository.findById(new ProcessLogId(processId.value()));

        if (processLogReadEntity.isPresent()) {
            throw new RuntimeException("Process log record already exists");
        }
    }
}
