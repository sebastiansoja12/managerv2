package com.warehouse.palletmanagement.domain.event;

import com.warehouse.commonassets.event.domain.model.DomainEvent;
import com.warehouse.palletmanagement.domain.vo.PalletUnloadedSnapshot;

import java.time.Instant;

public class PalletUnloaded implements DomainEvent {

    private final PalletUnloadedSnapshot snapshot;
    private final Instant timestamp;

    public PalletUnloaded(final PalletUnloadedSnapshot snapshot, final Instant timestamp) {
        this.snapshot = snapshot;
        this.timestamp = timestamp;
    }

    public PalletUnloadedSnapshot getSnapshot() {
        return snapshot;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }
}
