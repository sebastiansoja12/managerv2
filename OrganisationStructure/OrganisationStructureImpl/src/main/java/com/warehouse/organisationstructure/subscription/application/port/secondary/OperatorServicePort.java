package com.warehouse.organisationstructure.subscription.application.port.secondary;

import com.warehouse.commonassets.identificator.OperatorId;

public interface OperatorServicePort {

    boolean exists(final OperatorId operatorId);
}
