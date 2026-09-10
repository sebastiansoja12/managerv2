package com.warehouse.returning.infrastructure.adapter.primary.kafka.event;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.warehouse.returning.domain.vo.ShipmentId;
import com.warehouse.returning.domain.vo.UserId;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class ShipmentEvent {

    private final ShipmentSnapshot snapshot;
    private final Instant timestamp;
    private final UserId userId;
    private final OperatorId operatorId;

    protected ShipmentEvent(final ShipmentSnapshot snapshot,
                            final Instant timestamp,
                            final UserId userId,
                            final OperatorId operatorId) {
        this.snapshot = snapshot;
        this.timestamp = timestamp;
        this.userId = userId;
        this.operatorId = operatorId;
    }

    public ShipmentSnapshot snapshot() {
        return snapshot;
    }

    public Instant timestamp() {
        return timestamp;
    }

    public UserId userId() {
        return userId;
    }

    public OperatorId operatorId() {
        return operatorId;
    }

    public ShipmentId shipmentId() {
        return this.snapshot.shipmentId();
    }
}
