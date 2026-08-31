package com.warehouse.shipment.application.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.commonassets.event.domain.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.domain.model.IntegrationEvent;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.kafka.domain.model.OperatorAwareContext;
import com.warehouse.shipment.application.event.snapshot.ShipmentEventData;

@IntegrationEventType(value = "shipment.changed", version = 1)
public class ShipmentChangedIntegrationEvent extends OperatorAwareContext implements IntegrationEvent {

    public static final String TYPE = "shipment.changed";

    private ShipmentChangedEventPayload payload;

    @JsonCreator
    public ShipmentChangedIntegrationEvent(@JsonProperty("payload") final ShipmentChangedEventPayload payload) {
        this.payload = payload;
    }

    public ShipmentChangedIntegrationEvent(final ShipmentEventData shipment) {
        this(shipment, TYPE);
    }

    protected ShipmentChangedIntegrationEvent(final ShipmentEventData shipment, final String eventType) {
        this(ShipmentChangedEventPayload.from(shipment, eventType));
    }

    @JsonProperty("payload")
    public ShipmentChangedEventPayload payload() {
        return payload;
    }

    @Override
    public void assignOperatorContext(final OperatorId operatorId,
                                      final UserId userId,
                                      final DepartmentId departmentId) {
        super.assignOperatorContext(operatorId, userId, departmentId);
        this.payload = this.payload.withOperatorContext(operatorId, departmentId, userId);
    }
}
