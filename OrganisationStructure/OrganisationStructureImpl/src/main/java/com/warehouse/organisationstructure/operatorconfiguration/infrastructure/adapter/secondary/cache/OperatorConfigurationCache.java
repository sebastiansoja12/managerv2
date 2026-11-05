package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.cache;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;

import java.util.Optional;

public interface OperatorConfigurationCache {

    Optional<OperatorConfiguration> find(final OperatorId operatorId);

    void put(final OperatorId operatorId, final OperatorConfiguration configuration);

    void evict(final OperatorId operatorId);
}
