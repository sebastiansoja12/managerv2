package com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.routetracker.domain.vo.identifier.DepartmentId;
import com.warehouse.routetracker.domain.vo.identifier.OperatorId;
import com.warehouse.routetracker.domain.vo.identifier.UserId;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot.ShipmentEventData;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ShipmentChangedIntegrationEvent extends OperatorAwareContext {

    private final ShipmentEventData payload;
    private final String eventType;

    @JsonCreator
    public ShipmentChangedIntegrationEvent(
            @JsonProperty("payload") final ShipmentEventData payload,
            @JsonProperty("eventType") final String eventType,
            @JsonProperty("operatorId") final OperatorId operatorId,
            @JsonProperty("departmentId") final DepartmentId departmentId,
            @JsonProperty("userId") final UserId userId) {
        super(userId, departmentId, operatorId);
        this.payload = payload;
        this.eventType = eventType;
    }

    public ShipmentEventData payload() {
        return payload;
    }

    public String eventType() {
        return eventType;
    }
}
