package com.warehouse.returning.infrastructure.adapter.primary.kafka.event;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.returning.domain.vo.DepartmentCode;
import com.warehouse.returning.domain.vo.UserId;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipmentReturnCreated extends ShipmentEvent {

    private final String reasonCode;
    private final String reason;
    private final DepartmentCode departmentCode;

    @JsonCreator
    public ShipmentReturnCreated(@JsonProperty("snapshot") final ShipmentSnapshot snapshot,
                                 @JsonProperty("timestamp") final Instant timestamp,
                                 @JsonProperty("reasonCode") final String reasonCode,
                                 @JsonProperty("reason") final String reason,
                                 @JsonProperty("departmentCode") final DepartmentCode departmentCode,
                                 @JsonProperty("userId") final UserId userId,
                                 @JsonProperty("operatorId") final OperatorId operatorId) {
        super(snapshot, timestamp, userId, operatorId);
        this.reasonCode = reasonCode;
        this.reason = reason;
        this.departmentCode = departmentCode;
    }

    public String reasonCode() {
        return reasonCode;
    }

    public String reason() {
        return reason;
    }

    public DepartmentCode departmentCode() {
        return departmentCode;
    }
}
