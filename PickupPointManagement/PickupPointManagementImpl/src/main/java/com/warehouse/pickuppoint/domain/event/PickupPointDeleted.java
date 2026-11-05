package com.warehouse.pickuppoint.domain.event;

import com.warehouse.pickuppoint.domain.vo.PickupPointSnapshot;

import java.time.Instant;

public final class PickupPointDeleted extends PickupPointChanged {

    public PickupPointDeleted(final PickupPointSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }

    public PickupPointDeleted(final PickupPointSnapshot previousSnapshot,
                             final PickupPointSnapshot snapshot,
                             final Instant timestamp) {
        super(previousSnapshot, snapshot, timestamp);
    }
}
