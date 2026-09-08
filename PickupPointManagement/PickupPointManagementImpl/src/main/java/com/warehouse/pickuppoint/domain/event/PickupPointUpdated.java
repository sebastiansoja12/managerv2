package com.warehouse.pickuppoint.domain.event;

import com.warehouse.pickuppoint.domain.vo.PickupPointSnapshot;

import java.time.Instant;

public final class PickupPointUpdated extends PickupPointChanged {

    public PickupPointUpdated(final PickupPointSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
