package com.warehouse.shipment.application.event;

import com.warehouse.commonassets.event.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.shipment.application.event.snapshot.ShipmentEventData;

@IntegrationEventType(value = "shipment.created", version = 1)
public class ShipmentCreatedIntegrationEvent extends ShipmentChangedIntegrationEvent implements IntegrationEvent {

    public ShipmentCreatedIntegrationEvent(final ShipmentEventData shipmentEventData) {
        super(shipmentEventData);
    }
}
