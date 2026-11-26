package com.warehouse.pickuppoint.application.port.primary;

import com.warehouse.commonassets.identificator.PickupPointId;

import java.time.LocalDate;

public interface PickupPointReadModelSyncPort {

    void syncReadModel(final PickupPointId pickupPointId);

    int syncReadModels(final LocalDate dateFrom, final LocalDate dateTo);
}
