package com.warehouse.palletmanagement.domain.event;

import com.warehouse.commonassets.event.domain.model.DomainEvent;
import com.warehouse.palletmanagement.domain.vo.PalletDepartmentsChangedSnapshot;

import java.time.Instant;

public class PalletDepartmentsChanged implements DomainEvent {

    private final PalletDepartmentsChangedSnapshot snapshot;
    private final Instant timestamp;

    public PalletDepartmentsChanged(final PalletDepartmentsChangedSnapshot snapshot, final Instant timestamp) {
        this.snapshot = snapshot;
        this.timestamp = timestamp;
    }

    public PalletDepartmentsChangedSnapshot getSnapshot() {
        return snapshot;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }
}
