package com.warehouse.organisationstructure.operatorconfiguration.domain.service;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.port.secondary.OperatorConfigurationRepository;

import java.util.Optional;

public class OperatorConfigurationServiceImpl implements OperatorConfigurationService {

    private final OperatorConfigurationRepository operatorConfigurationRepository;

    public OperatorConfigurationServiceImpl(final OperatorConfigurationRepository operatorConfigurationRepository) {
        this.operatorConfigurationRepository = operatorConfigurationRepository;
    }

    @Override
    public Optional<OperatorConfiguration> getByOperatorId(final OperatorId operatorId) {
        return operatorConfigurationRepository.findByOperatorId(operatorId);
    }

    @Override
    public OperatorConfiguration create(final OperatorId operatorId, final OperatorConfiguration configuration) {
        return operatorConfigurationRepository.save(operatorId, configuration);
    }
}
