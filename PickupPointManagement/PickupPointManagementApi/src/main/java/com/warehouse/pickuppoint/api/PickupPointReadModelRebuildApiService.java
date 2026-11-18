package com.warehouse.pickuppoint.api;

import com.warehouse.commonassets.identificator.OperatorId;

import java.time.LocalDate;

public interface PickupPointReadModelRebuildApiService {

    int rebuildReadModels(final OperatorId operatorId, final LocalDate dateFrom, final LocalDate dateTo);
}
