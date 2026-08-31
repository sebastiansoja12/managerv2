package com.warehouse.shipment.application.event;

import com.warehouse.commonassets.event.domain.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.domain.model.IntegrationEvent;
import com.warehouse.shipment.application.event.snapshot.ShipmentEventData;

@IntegrationEventType(value = "shipment.canceled", version = 1)
public class ShipmentCanceledMessage extends ShipmentChangedIntegrationEvent implements IntegrationEvent {

    public static final String TYPE = "shipment.canceled";

    public ShipmentCanceledMessage(final ShipmentEventData shipment) {
        super(shipment, TYPE);
    }
}
