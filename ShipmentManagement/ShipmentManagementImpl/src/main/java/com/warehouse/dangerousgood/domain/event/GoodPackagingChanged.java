package com.warehouse.dangerousgood.domain.event;

import com.warehouse.dangerousgood.domain.vo.GoodSnapshot;

import java.time.Instant;

public class GoodPackagingChanged extends GoodChangedEvent implements GoodEvent {
    public GoodPackagingChanged(final GoodSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
