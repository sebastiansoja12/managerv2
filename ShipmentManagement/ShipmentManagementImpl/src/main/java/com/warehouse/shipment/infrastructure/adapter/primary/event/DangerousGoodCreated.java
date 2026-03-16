package com.warehouse.shipment.infrastructure.adapter.primary.event;

import java.time.Instant;

import com.warehouse.dangerousgood.domain.vo.GoodSnapshot;

public class DangerousGoodCreated extends DangerousGoodChanged implements DangerousGoodEvent {
    public DangerousGoodCreated(final GoodSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
