package com.warehouse.pickuppoint.application.port.secondary;

import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.pickuppoint.domain.vo.PickupPointSnapshot;

public interface PickupPointReadModelRepository {

    void sync(final PickupPointSnapshot snapshot);

    boolean exists(final PickupPointId pickupPointId);
}
