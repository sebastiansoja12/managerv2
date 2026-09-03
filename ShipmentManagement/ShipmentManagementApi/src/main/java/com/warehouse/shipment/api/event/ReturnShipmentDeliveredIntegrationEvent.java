package com.warehouse.shipment.api.event;

import com.warehouse.commonassets.event.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.commonassets.event.integration.model.IntegrationEventKey;
import com.warehouse.commonassets.identificator.ReturnId;
import com.warehouse.commonassets.identificator.ShipmentId;

import java.time.Instant;

@IntegrationEventType(value = "return.shipment.delivered", version = 1)
public record ReturnShipmentDeliveredIntegrationEvent(ReturnId returnId, ShipmentId originalShipmentId,
                                                      ShipmentId returnShipmentId, Instant occurredAt)
        implements IntegrationEvent, IntegrationEventKey {
    @Override
    public String eventKey() {
        return String.valueOf(returnId.getId());
    }
}
