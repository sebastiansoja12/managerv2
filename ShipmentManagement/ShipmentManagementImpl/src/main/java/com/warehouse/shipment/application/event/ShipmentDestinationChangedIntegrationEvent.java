package com.warehouse.shipment.application.event;

import com.warehouse.commonassets.event.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.shipment.application.event.snapshot.ShipmentEventData;

@IntegrationEventType(value = "shipment.destination.changed", version = 1)
public class ShipmentDestinationChangedIntegrationEvent extends ShipmentChangedIntegrationEvent implements IntegrationEvent {

    public ShipmentDestinationChangedIntegrationEvent(final ShipmentEventData shipmentEventData) {
        super(shipmentEventData);
    }
}
