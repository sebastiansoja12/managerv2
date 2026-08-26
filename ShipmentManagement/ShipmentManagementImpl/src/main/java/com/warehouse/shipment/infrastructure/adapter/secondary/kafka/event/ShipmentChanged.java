package com.warehouse.shipment.infrastructure.adapter.secondary.kafka.event;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.kafka.domain.model.OperatorAwareContext;
import com.warehouse.commonassets.kafka.infrastructure.annotation.KafkaTopic;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

@JsonIgnoreProperties(ignoreUnknown = true)
@KafkaTopic("${manager.kafka.topics.shipment-events:shipment.events}")
public class ShipmentChanged extends OperatorAwareContext {

    private final ShipmentSnapshot snapshot;
    private final Instant timestamp;

    public ShipmentChanged(final ShipmentSnapshot snapshot,
                           final Instant timestamp) {
        super();
        this.snapshot = snapshot;
        this.timestamp = timestamp;
    }

    public ShipmentChanged(final ShipmentSnapshot snapshot,
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
}
