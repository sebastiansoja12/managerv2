package com.warehouse.organisationstructure.subscription.application.port.secondary;

import com.warehouse.commonassets.identificator.OperatorId;

import java.util.Optional;

public interface CurrentOperatorServicePort {

    Optional<OperatorId> currentOperatorId();
}
