package com.warehouse.logisticsdelivery.infrastructure.adapter.primary;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.logisticsdelivery.api.DeliveryReadModelRebuildApiService;
import com.warehouse.logisticsdelivery.application.port.primary.DeliveryReadModelPort;

import java.time.LocalDate;

public class DeliveryReadModelRebuildApiServiceAdapter implements DeliveryReadModelRebuildApiService {

    private final DeliveryReadModelPort deliveryReadModelPort;
    private final OperatorContext operatorContext;

    public DeliveryReadModelRebuildApiServiceAdapter(final DeliveryReadModelPort deliveryReadModelPort,
                                                     final OperatorContext operatorContext) {
        this.deliveryReadModelPort = deliveryReadModelPort;
        this.operatorContext = operatorContext;
    }

    @Override
    public int rebuildReadModels(final OperatorId operatorId, final LocalDate dateFrom, final LocalDate dateTo) {
        return this.operatorContext.runAs(operatorId,
                () -> this.deliveryReadModelPort.syncReadModels(dateFrom, dateTo));
    }
}
