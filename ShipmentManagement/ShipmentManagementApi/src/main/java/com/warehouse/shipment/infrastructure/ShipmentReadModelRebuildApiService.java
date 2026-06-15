package com.warehouse.shipment.infrastructure;

import com.warehouse.commonassets.identificator.OperatorId;

import java.time.LocalDate;

public interface ShipmentReadModelRebuildApiService {

    int rebuildReadModels(final OperatorId operatorId, final LocalDate dateFrom, final LocalDate dateTo);
}
