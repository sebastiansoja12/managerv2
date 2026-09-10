package com.warehouse.pickuppoint.application.port.primary;

import com.warehouse.commonassets.identificator.PickupPointId;

public interface PickupPointReadModelSyncPort {

    void syncReadModel(final PickupPointId pickupPointId);
}
