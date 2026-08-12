package com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.kafka.domain.model.OperatorAwareEvent;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipmentReturned extends ShipmentEvent implements OperatorAwareEvent {

    private final String reasonCode;
    private final String reason;

    @JsonCreator
    public ShipmentReturned(@JsonProperty("snapshot") final ShipmentSnapshot snapshot,
                            @JsonProperty("timestamp") final Instant timestamp,
                            @JsonProperty("reasonCode") final String reasonCode,
                            @JsonProperty("reason") final String reason,
                            @JsonProperty("userId") final UserId userId,
                            @JsonProperty("departmentId") final DepartmentId departmentId,
                            @JsonProperty("operatorId") final OperatorId operatorId) {
        super(snapshot, timestamp, userId, departmentId, operatorId);
        this.reasonCode = reasonCode;
        this.reason = reason;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public String getReason() {
        return reason;
    }
}
