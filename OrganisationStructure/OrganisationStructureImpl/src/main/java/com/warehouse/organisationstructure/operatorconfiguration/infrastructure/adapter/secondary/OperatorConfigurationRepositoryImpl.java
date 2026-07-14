package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.port.secondary.OperatorConfigurationRepository;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.mapper.OperatorConfigurationMapper;

import java.util.Optional;

public class OperatorConfigurationRepositoryImpl implements OperatorConfigurationRepository {

    private final OperatorConfigurationReadRepository repository;

    public OperatorConfigurationRepositoryImpl(final OperatorConfigurationReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<OperatorConfiguration> findByOperatorId(final OperatorId operatorId) {
        return repository.findById(operatorId)
                .map(OperatorConfigurationMapper::toModel);
    }

    @Override
    public OperatorConfiguration save(final OperatorId operatorId, final OperatorConfiguration configuration) {
        return repository.save(OperatorConfigurationMapper.toEntity(operatorId, configuration))
                .toModel();
    }
}
