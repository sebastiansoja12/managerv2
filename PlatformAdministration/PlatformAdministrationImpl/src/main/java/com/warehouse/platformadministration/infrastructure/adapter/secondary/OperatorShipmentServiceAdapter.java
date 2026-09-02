package com.warehouse.platformadministration.infrastructure.adapter.secondary;

import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.platformadministration.application.port.secondary.OperatorShipmentServicePort;
import com.warehouse.platformadministration.domain.vo.OperatorScope;

public class OperatorShipmentServiceAdapter implements OperatorShipmentServicePort {

    private final ShipmentApiService shipmentApiService;

    private final OperatorScopeExecutor operatorScopeExecutor;

    public OperatorShipmentServiceAdapter(final ShipmentApiService shipmentApiService,
                                          final OperatorScopeExecutor operatorScopeExecutor) {
        this.shipmentApiService = shipmentApiService;
        this.operatorScopeExecutor = operatorScopeExecutor;
    }

    @Override
    public boolean hasAnyShipment(final OperatorScope operatorScope) {
        return operatorScopeExecutor.execute(operatorScope, shipmentApiService::hasAnyShipment);
    }
}
