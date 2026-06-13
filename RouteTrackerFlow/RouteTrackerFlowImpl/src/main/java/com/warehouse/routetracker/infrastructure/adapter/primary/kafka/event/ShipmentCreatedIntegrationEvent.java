package com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.routetracker.domain.vo.identifier.DepartmentId;
import com.warehouse.routetracker.domain.vo.identifier.OperatorId;
import com.warehouse.routetracker.domain.vo.identifier.UserId;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot.ShipmentSnapshot;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ShipmentCreatedIntegrationEvent extends ShipmentChangedIntegrationEvent {

    @JsonCreator
    public ShipmentCreatedIntegrationEvent(
            @JsonProperty("snapshot") final ShipmentSnapshot snapshot,
            @JsonProperty("userId") final UserId userId,
            @JsonProperty("departmentId") final DepartmentId departmentId,
            @JsonProperty("operatorId") final OperatorId operatorId) {
        super(snapshot, userId, departmentId, operatorId);
    }
}
