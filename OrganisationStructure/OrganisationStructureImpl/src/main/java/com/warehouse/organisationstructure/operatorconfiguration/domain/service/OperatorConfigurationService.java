package com.warehouse.organisationstructure.operatorconfiguration.domain.service;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;

import java.util.Optional;

public interface OperatorConfigurationService {

    Optional<OperatorConfiguration> getByOperatorId(final OperatorId operatorId);

    OperatorConfiguration create(final OperatorId operatorId, final OperatorConfiguration configuration);
}
