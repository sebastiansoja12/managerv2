package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.api.OperatorApiService;
import com.warehouse.shipment.application.port.secondary.OperatorServicePort;

public class OperatorServiceAdapter implements OperatorServicePort {

    private final OperatorApiService operatorApiService;

    public OperatorServiceAdapter(final OperatorApiService operatorApiService) {
        this.operatorApiService = operatorApiService;
    }

    @Override
    public boolean isActive(final OperatorId operatorId) {
        return this.operatorApiService.isActive(operatorId);
    }
}
