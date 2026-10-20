package com.warehouse.asyncjob.infrastructure.adapter.secondary;

import com.warehouse.asyncjob.application.port.secondary.ReadModelRebuildServicePort;
import com.warehouse.asyncjob.domain.enumeration.ReadModelType;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.shipment.infrastructure.ShipmentReadModelRebuildApiService;

import java.time.LocalDate;

public class ShipmentReadModelRebuildServiceAdapter implements ReadModelRebuildServicePort {

    private final ShipmentReadModelRebuildApiService rebuildApiService;

    public ShipmentReadModelRebuildServiceAdapter(final ShipmentReadModelRebuildApiService rebuildApiService) {
        this.rebuildApiService = rebuildApiService;
    }

    @Override
    public ReadModelType type() {
        return ReadModelType.SHIPMENT;
    }

    @Override
    public void rebuild(final OperatorId operatorId, final LocalDate dateFrom, final LocalDate dateTo) {
        this.rebuildApiService.rebuildReadModels(operatorId, dateFrom, dateTo);
    }
}
