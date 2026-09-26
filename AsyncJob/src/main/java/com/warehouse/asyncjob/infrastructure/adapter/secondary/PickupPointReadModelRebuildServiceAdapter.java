package com.warehouse.asyncjob.infrastructure.adapter.secondary;

import com.warehouse.asyncjob.application.port.secondary.ReadModelRebuildServicePort;
import com.warehouse.asyncjob.domain.enumeration.ReadModelType;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.pickuppoint.api.PickupPointReadModelRebuildApiService;

import java.time.LocalDate;

public class PickupPointReadModelRebuildServiceAdapter implements ReadModelRebuildServicePort {

    private final PickupPointReadModelRebuildApiService rebuildApiService;

    public PickupPointReadModelRebuildServiceAdapter(final PickupPointReadModelRebuildApiService rebuildApiService) {
        this.rebuildApiService = rebuildApiService;
    }

    @Override
    public ReadModelType type() {
        return ReadModelType.PICKUP_POINT;
    }

    @Override
    public void rebuild(final OperatorId operatorId, final LocalDate dateFrom, final LocalDate dateTo) {
        this.rebuildApiService.rebuildReadModels(operatorId, dateFrom, dateTo);
    }
}
