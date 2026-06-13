package com.warehouse.shipment.infrastructure.adapter.primary;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.shipment.application.port.primary.ShipmentReadModelSyncPort;
import com.warehouse.shipment.infrastructure.ShipmentReadModelRebuildApiService;

@Service
public class ShipmentReadModelRebuildApiServiceAdapter implements ShipmentReadModelRebuildApiService {

    private final ShipmentReadModelSyncPort syncPort;
    private final OperatorContext operatorContext;

    public ShipmentReadModelRebuildApiServiceAdapter(final ShipmentReadModelSyncPort syncPort,
                                                     final OperatorContext operatorContext) {
        this.syncPort = syncPort;
        this.operatorContext = operatorContext;
    }

    @Override
    public int rebuildReadModels(final OperatorId operatorId,
                                 final LocalDate dateFrom,
                                 final LocalDate dateTo) {
        return this.operatorContext.runAs(operatorId, this.syncPort::syncReadModels);
    }
}
