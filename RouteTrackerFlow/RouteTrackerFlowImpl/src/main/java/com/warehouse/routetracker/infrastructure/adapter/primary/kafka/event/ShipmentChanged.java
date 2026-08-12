package com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipmentChanged extends ShipmentEvent {

    @JsonCreator
    public ShipmentChanged(@JsonProperty("snapshot") final ShipmentSnapshot snapshot,
                           @JsonProperty("timestamp") final Instant timestamp,
                           @JsonProperty("userId") final UserId userId,
                           @JsonProperty("departmentId") final DepartmentId departmentId,
                           @JsonProperty("operatorId") final OperatorId operatorId) {
        super(snapshot, timestamp, userId, departmentId, operatorId);
    }
}
