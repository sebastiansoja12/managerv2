package com.warehouse.logisticsdelivery.domain.event;

import com.warehouse.logisticsdelivery.domain.vo.DeliverySnapshot;

import java.time.Instant;

public final class DeliveryModified extends DeliveryChanged {

    public DeliveryModified(final DeliverySnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
