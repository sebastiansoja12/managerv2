package com.warehouse.shipment.infrastructure.adapter.primary;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.shipment.domain.service.ShipmentReadModelSyncService;
import com.warehouse.shipment.infrastructure.ShipmentReadModelRebuildApiService;

@Service
public class ShipmentReadModelRebuildApiServiceAdapter implements ShipmentReadModelRebuildApiService {

    private final ShipmentReadModelSyncService syncService;
    private final OperatorContext operatorContext;

    public ShipmentReadModelRebuildApiServiceAdapter(final ShipmentReadModelSyncService syncService,
                                                     final OperatorContext operatorContext) {
        this.syncService = syncService;
        this.operatorContext = operatorContext;
    }

    @Override
    public int rebuildReadModels(final OperatorId operatorId,
                                 final LocalDate dateFrom,
                                 final LocalDate dateTo) {
        return this.operatorContext.runAs(operatorId, this.syncService::syncReadModels);
    }
}
