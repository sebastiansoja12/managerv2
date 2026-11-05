package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.cache;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;

import java.util.Optional;

public class NoOpOperatorConfigurationCache implements OperatorConfigurationCache {

    @Override
    public Optional<OperatorConfiguration> find(final OperatorId operatorId) {
        return Optional.empty();
    }

    @Override
    public void put(final OperatorId operatorId, final OperatorConfiguration configuration) {
    }

    @Override
    public void evict(final OperatorId operatorId) {
    }
}
