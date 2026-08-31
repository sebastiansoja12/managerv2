package com.warehouse.shipment.application.event;

import com.warehouse.commonassets.event.domain.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.domain.model.IntegrationEvent;
import com.warehouse.shipment.application.event.snapshot.ShipmentEventData;

@IntegrationEventType(value = "shipment.destination.changed", version = 1)
public class ShipmentDestinationChangedIntegrationEvent extends ShipmentChangedIntegrationEvent implements IntegrationEvent {

    public static final String TYPE = "shipment.destination.changed";

    public ShipmentDestinationChangedIntegrationEvent(final ShipmentEventData shipment) {
        super(shipment, TYPE);
    }
}
