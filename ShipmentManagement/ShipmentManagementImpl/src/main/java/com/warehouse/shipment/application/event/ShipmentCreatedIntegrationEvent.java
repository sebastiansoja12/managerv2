package com.warehouse.shipment.application.event;

import com.warehouse.commonassets.event.domain.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.domain.model.IntegrationEvent;
import com.warehouse.shipment.application.event.snapshot.ShipmentEventData;

@IntegrationEventType(value = "shipment.created", version = 1)
public class ShipmentCreatedIntegrationEvent extends ShipmentChangedIntegrationEvent implements IntegrationEvent {

    public static final String TYPE = "shipment.created";

    public ShipmentCreatedIntegrationEvent(final ShipmentEventData shipmentEventData) {
        super(shipmentEventData, TYPE);
    }
}
