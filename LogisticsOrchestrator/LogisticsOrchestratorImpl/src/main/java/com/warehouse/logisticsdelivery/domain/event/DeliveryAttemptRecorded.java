package com.warehouse.logisticsdelivery.domain.event;

import com.warehouse.logisticsdelivery.domain.vo.DeliverySnapshot;

import java.time.Instant;

public final class DeliveryAttemptRecorded extends DeliveryChanged {

    public DeliveryAttemptRecorded(final DeliverySnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
