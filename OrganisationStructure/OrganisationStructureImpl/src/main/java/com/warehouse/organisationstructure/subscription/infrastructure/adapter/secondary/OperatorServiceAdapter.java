package com.warehouse.organisationstructure.subscription.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.subscription.application.port.secondary.OperatorServicePort;
import com.warehouse.organisationstructure.api.OperatorApiService;

public class OperatorServiceAdapter implements OperatorServicePort {

    private final OperatorApiService operatorApiService;

    public OperatorServiceAdapter(final OperatorApiService operatorApiService) {
        this.operatorApiService = operatorApiService;
    }

    @Override
    public boolean exists(final OperatorId operatorId) {
        return operatorApiService.exists(operatorId);
    }
}
