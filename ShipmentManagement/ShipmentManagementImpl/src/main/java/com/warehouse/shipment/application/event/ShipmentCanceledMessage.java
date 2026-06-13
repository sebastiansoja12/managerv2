package com.warehouse.shipment.application.event;

import com.warehouse.commonassets.event.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.shipment.application.event.snapshot.ShipmentEventData;

@IntegrationEventType(value = "shipment.canceled", version = 1)
public class ShipmentCanceledMessage extends ShipmentChangedIntegrationEvent implements IntegrationEvent {

    public ShipmentCanceledMessage(final ShipmentEventData shipmentEventData) {
        super(shipmentEventData);
    }
}
