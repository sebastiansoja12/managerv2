package com.warehouse.organisationstructure.operator.domain.event;

import com.warehouse.organisationstructure.operator.domain.vo.OperatorSnapshot;

import java.time.Instant;

public class OperatorChangedEvent implements OperatorEvent {

    private final OperatorSnapshot snapshot;
    private final Instant timestamp;

    public OperatorChangedEvent(final OperatorSnapshot snapshot, final Instant timestamp) {
        this.snapshot = snapshot;
        this.timestamp = timestamp;
    }

    @Override
    public OperatorSnapshot getSnapshot() {
        return snapshot;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }
}
