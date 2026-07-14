package com.warehouse.organisationstructure.operatorconfiguration.domain.port.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;

import java.util.Optional;

public interface OperatorConfigurationRepository {

    Optional<OperatorConfiguration> findByOperatorId(final OperatorId operatorId);

    OperatorConfiguration save(final OperatorId operatorId, final OperatorConfiguration configuration);
}
