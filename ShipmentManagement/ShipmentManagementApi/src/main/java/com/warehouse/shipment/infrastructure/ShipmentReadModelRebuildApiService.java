package com.warehouse.shipment.infrastructure;

import java.time.LocalDate;

import com.warehouse.commonassets.identificator.OperatorId;

public interface ShipmentReadModelRebuildApiService {

    int rebuildReadModels(OperatorId operatorId, LocalDate dateFrom, LocalDate dateTo);
}
