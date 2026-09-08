package com.warehouse.logisticsdelivery.api;

import com.warehouse.commonassets.identificator.OperatorId;

import java.time.LocalDate;

public interface DeliveryReadModelRebuildApiService {

    int rebuildReadModels(final OperatorId operatorId, final LocalDate dateFrom, final LocalDate dateTo);
}
