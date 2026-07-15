package com.warehouse.pickuppoint.domain.event;

import com.warehouse.pickuppoint.domain.vo.PickupPointSnapshot;

import java.time.Instant;

public final class PickupPointClosed extends PickupPointChanged {

    public PickupPointClosed(final PickupPointSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
