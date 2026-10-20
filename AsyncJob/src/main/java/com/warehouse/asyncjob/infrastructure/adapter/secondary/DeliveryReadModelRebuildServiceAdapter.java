package com.warehouse.asyncjob.infrastructure.adapter.secondary;

import com.warehouse.asyncjob.application.port.secondary.ReadModelRebuildServicePort;
import com.warehouse.asyncjob.domain.enumeration.ReadModelType;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.logisticsdelivery.api.DeliveryReadModelRebuildApiService;

import java.time.LocalDate;

public class DeliveryReadModelRebuildServiceAdapter implements ReadModelRebuildServicePort {

    private final DeliveryReadModelRebuildApiService rebuildApiService;

    public DeliveryReadModelRebuildServiceAdapter(final DeliveryReadModelRebuildApiService rebuildApiService) {
        this.rebuildApiService = rebuildApiService;
    }

    @Override
    public ReadModelType type() {
        return ReadModelType.DELIVERY;
    }

    @Override
    public void rebuild(final OperatorId operatorId, final LocalDate dateFrom, final LocalDate dateTo) {
        this.rebuildApiService.rebuildReadModels(operatorId, dateFrom, dateTo);
    }
}
