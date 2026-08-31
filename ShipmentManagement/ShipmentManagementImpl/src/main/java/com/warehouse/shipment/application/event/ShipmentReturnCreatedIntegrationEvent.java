package com.warehouse.shipment.application.event;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.commonassets.event.domain.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.domain.model.IntegrationEvent;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.kafka.domain.model.OperatorAwareContext;
import com.warehouse.shipment.application.event.snapshot.ShipmentEventData;

@IntegrationEventType(value = "shipment.return.created", version = 1)
public class ShipmentReturnCreatedIntegrationEvent extends OperatorAwareContext implements IntegrationEvent {

    private final ShipmentEventData snapshot;
    private final Instant timestamp;
    private final String reasonCode;
    private final String reason;
    private final DepartmentCode departmentCode;

    @JsonCreator
    public ShipmentReturnCreatedIntegrationEvent(
            @JsonProperty("snapshot") final ShipmentEventData snapshot,
            @JsonProperty("timestamp") final Instant timestamp,
            @JsonProperty("reasonCode") final String reasonCode,
            @JsonProperty("reason") final String reason,
            @JsonProperty("departmentCode") final DepartmentCode departmentCode) {
        this.snapshot = snapshot;
        this.timestamp = timestamp;
        this.reasonCode = reasonCode;
        this.reason = reason;
        this.departmentCode = departmentCode;
    }

    public ShipmentEventData getSnapshot() {
        return snapshot;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public String getReason() {
        return reason;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }
}
