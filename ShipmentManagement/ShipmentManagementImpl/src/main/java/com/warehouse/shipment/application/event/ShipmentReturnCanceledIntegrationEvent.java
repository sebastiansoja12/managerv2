package com.warehouse.shipment.application.event;

import com.warehouse.commonassets.event.domain.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.domain.model.IntegrationEvent;
import com.warehouse.shipment.application.event.snapshot.ShipmentEventData;

@IntegrationEventType(value = "shipment.return.canceled", version = 1)
public class ShipmentReturnCanceledIntegrationEvent extends ShipmentChangedIntegrationEvent implements IntegrationEvent {

    public static final String TYPE = "shipment.return.canceled";

    public ShipmentReturnCanceledIntegrationEvent(final ShipmentEventData shipment) {
        super(shipment, TYPE);
    }
}
