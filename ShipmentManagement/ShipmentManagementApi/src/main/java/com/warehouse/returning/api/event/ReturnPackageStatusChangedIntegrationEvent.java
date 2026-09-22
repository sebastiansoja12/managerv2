package com.warehouse.returning.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.commonassets.enumeration.ReturnStatus;
import com.warehouse.commonassets.event.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.commonassets.event.integration.model.IntegrationEventKey;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;

import java.time.Instant;

@IntegrationEventType(value = "return.package.status.changed", version = 2)
public class ReturnPackageStatusChangedIntegrationEvent implements IntegrationEvent, IntegrationEventKey {

    private final ShipmentId shipmentId;
    private final ReturnStatus returnStatus;
    private final String reason;
    private final String reasonCode;
    private final DepartmentId departmentId;
    private final UserId assignedTo;
    private final UserId processedBy;
    private final Instant timestamp;

    @JsonCreator
    public ReturnPackageStatusChangedIntegrationEvent(
            @JsonProperty("shipmentId") final ShipmentId shipmentId,
            @JsonProperty("returnStatus") final ReturnStatus returnStatus,
            @JsonProperty("reason") final String reason,
            @JsonProperty("reasonCode") final String reasonCode,
            @JsonProperty("departmentId") final DepartmentId departmentId,
            @JsonProperty("assignedTo") final UserId assignedTo,
            @JsonProperty("processedBy") final UserId processedBy,
            @JsonProperty("timestamp") final Instant timestamp) {
        this.shipmentId = shipmentId;
        this.returnStatus = returnStatus;
        this.reason = reason;
        this.reasonCode = reasonCode;
        this.departmentId = departmentId;
        this.assignedTo = assignedTo;
        this.processedBy = processedBy;
        this.timestamp = timestamp;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public ReturnStatus getReturnStatus() {
        return returnStatus;
    }

    public String getReason() {
        return reason;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public DepartmentId getDepartmentId() {
        return departmentId;
    }

    public UserId getAssignedTo() {
        return assignedTo;
    }

    public UserId getProcessedBy() {
        return processedBy;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public String eventKey() {
        return String.valueOf(this.shipmentId.getValue());
    }
}
