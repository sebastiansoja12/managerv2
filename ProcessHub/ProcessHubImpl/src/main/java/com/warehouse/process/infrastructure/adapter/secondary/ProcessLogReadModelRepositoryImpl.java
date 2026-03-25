package com.warehouse.process.infrastructure.adapter.secondary;

import java.util.Optional;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;
import com.warehouse.process.domain.model.ProcessLog;
import com.warehouse.process.domain.port.secondary.ProcessLogReadModelRepository;
import com.warehouse.process.infrastructure.adapter.secondary.entity.read.ProcessLogReadEntity;
import com.warehouse.process.infrastructure.adapter.secondary.mapper.ProcessLogToModelMapper;
import com.warehouse.process.infrastructure.adapter.secondary.mapper.ProcessLogToReadEntityMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class ProcessLogReadModelRepositoryImpl implements ProcessLogReadModelRepository {

    private final ProcessLogReadRepository processLogRepository;

    public ProcessLogReadModelRepositoryImpl(final ProcessLogReadRepository processLogRepository) {
        this.processLogRepository = processLogRepository;
    }

    @Override
    public void createProcessLog(final ProcessLog processLog) {
        final ProcessLogReadEntity entity = ProcessLogToReadEntityMapper.map(processLog);
        assignOperator(entity, getOperatorId());

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
        assignOperator(entity, getOperatorId());
        processLogRepository.save(entity);
    }

    private void assignOperator(final ProcessLogReadEntity entity, final OperatorId operatorId) {
        entity.assignOperator(operatorId);
        entity.getCommunicationLogs().forEach(log -> log.assignOperator(operatorId));
    }

    private OperatorId getOperatorId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof UsernameTenantPasswordAuthenticationToken token) {
            return token.getOperatorId();
        }

        throw new IllegalStateException("No operator id found");
    }
}
