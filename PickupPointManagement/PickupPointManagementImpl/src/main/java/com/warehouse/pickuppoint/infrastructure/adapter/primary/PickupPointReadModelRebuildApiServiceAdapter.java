package com.warehouse.pickuppoint.infrastructure.adapter.primary;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.pickuppoint.api.PickupPointReadModelRebuildApiService;
import com.warehouse.pickuppoint.application.port.primary.PickupPointReadModelSyncPort;

import java.time.LocalDate;

public class PickupPointReadModelRebuildApiServiceAdapter implements PickupPointReadModelRebuildApiService {

    private final PickupPointReadModelSyncPort pickupPointReadModelSyncPort;
    private final OperatorContext operatorContext;

    public PickupPointReadModelRebuildApiServiceAdapter(final PickupPointReadModelSyncPort pickupPointReadModelSyncPort,
                                                        final OperatorContext operatorContext) {
        this.pickupPointReadModelSyncPort = pickupPointReadModelSyncPort;
        this.operatorContext = operatorContext;
    }

    @Override
    public int rebuildReadModels(final OperatorId operatorId, final LocalDate dateFrom, final LocalDate dateTo) {
        return this.operatorContext.runAs(operatorId, this.pickupPointReadModelSyncPort::syncReadModels);
    }
}
