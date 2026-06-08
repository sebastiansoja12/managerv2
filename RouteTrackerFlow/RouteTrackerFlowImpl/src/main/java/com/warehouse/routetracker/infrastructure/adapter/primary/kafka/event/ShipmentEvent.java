package com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.kafka.domain.model.OperatorAwareContext;

import java.time.Instant;

public abstract class ShipmentEvent extends OperatorAwareContext {

    private final ShipmentSnapshot snapshot;
    private final Instant timestamp;

    protected ShipmentEvent(final ShipmentSnapshot snapshot,
                            final Instant timestamp,
                            final UserId userId,
                            final DepartmentId departmentId,
                            final OperatorId operatorId) {
        super(userId, departmentId, operatorId);
        this.snapshot = snapshot;
        this.timestamp = timestamp;
    }

    public ShipmentSnapshot getSnapshot() {
        return snapshot;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public ShipmentSnapshot snapshot() {
        return snapshot;
    }

    public Instant timestamp() {
        return timestamp;
    }

    public Long shipmentId() {
        return this.snapshot().shipmentId().value();
    }

    public String shipmentStatus() {
        return this.snapshot().shipmentStatus();
    }

}
