package com.warehouse.asyncjob.infrastructure.adapter.secondary.rebuilder;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.warehouse.asyncjob.domain.model.ReadModelType;
import com.warehouse.asyncjob.domain.service.ReadModelRebuilder;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.shipment.infrastructure.ShipmentReadModelRebuildApiService;

@Component
public class ShipmentReadModelRebuilder implements ReadModelRebuilder {

    private final ShipmentReadModelRebuildApiService rebuildApiService;

    public ShipmentReadModelRebuilder(final ShipmentReadModelRebuildApiService rebuildApiService) {
        this.rebuildApiService = rebuildApiService;
    }

    @Override
    public ReadModelType type() {
        return ReadModelType.SHIPMENT;
    }

    @Override
    public void rebuild(final Long operatorId,
                        final LocalDate dateFrom,
                        final LocalDate dateTo) {
        this.rebuildApiService.rebuildReadModels(OperatorId.of(operatorId), dateFrom, dateTo);
    }
}
