package com.warehouse.organisationstructure.operatorconfiguration.domain.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;

import java.util.Optional;

public interface OperatorConfigurationPort {

    Optional<OperatorConfiguration> getCurrent();

    void create(final OperatorId operatorId, final OperatorConfiguration configuration);
}
