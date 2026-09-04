package com.warehouse.palletmanagement.domain.event;

import com.warehouse.commonassets.event.domain.model.DomainEvent;
import com.warehouse.palletmanagement.domain.vo.PalletLoadedSnapshot;

import java.time.Instant;

public class PalletLoaded implements DomainEvent {

    private final PalletLoadedSnapshot snapshot;
    private final Instant timestamp;

    public PalletLoaded(final PalletLoadedSnapshot snapshot, final Instant timestamp) {
        this.snapshot = snapshot;
        this.timestamp = timestamp;
    }

    public PalletLoadedSnapshot getSnapshot() {
        return snapshot;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }
}
