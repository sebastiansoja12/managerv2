package com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.routetracker.domain.vo.identifier.DepartmentId;
import com.warehouse.routetracker.domain.vo.identifier.OperatorId;
import com.warehouse.routetracker.domain.vo.identifier.UserId;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot.ShipmentSnapshot;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ShipmentStatusChangedIntegrationEvent extends ShipmentChangedIntegrationEvent {

    @JsonCreator
    public ShipmentStatusChangedIntegrationEvent(
            @JsonProperty("payload") final ShipmentSnapshot payload,
            @JsonProperty("userId") final UserId userId,
            @JsonProperty("departmentId") final DepartmentId departmentId,
            @JsonProperty("operatorId") final OperatorId operatorId) {
        super(payload, userId, departmentId, operatorId);
    }
}
