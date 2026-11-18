package com.warehouse.palletmanagement.domain.event;

import com.warehouse.commonassets.event.domain.model.DomainEvent;
import com.warehouse.palletmanagement.domain.vo.PalletLoadingRevertedSnapshot;

import java.time.Instant;

public class PalletLoadingReverted implements DomainEvent {

    private final PalletLoadingRevertedSnapshot snapshot;
    private final Instant timestamp;

    public PalletLoadingReverted(final PalletLoadingRevertedSnapshot snapshot, final Instant timestamp) {
        this.snapshot = snapshot;
        this.timestamp = timestamp;
    }

    public PalletLoadingRevertedSnapshot getSnapshot() {
        return snapshot;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }
}
