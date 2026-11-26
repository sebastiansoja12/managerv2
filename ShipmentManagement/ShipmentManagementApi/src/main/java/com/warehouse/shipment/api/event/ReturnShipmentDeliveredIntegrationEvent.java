package com.warehouse.shipment.api.event;

import com.warehouse.eventoutbox.integration.annotation.IntegrationEventType;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;
import com.warehouse.eventoutbox.integration.model.IntegrationEventKey;
import com.warehouse.commonassets.identificator.ReturnId;
import com.warehouse.commonassets.identificator.ShipmentId;

import java.time.Instant;

@IntegrationEventType(value = "return.shipment.delivered", version = 1, topic = ShipmentTopics.RETURN_SHIPMENT_DELIVERED)
public record ReturnShipmentDeliveredIntegrationEvent(ReturnId returnId, ShipmentId originalShipmentId,
                                                      ShipmentId returnShipmentId, Instant occurredAt)
        implements IntegrationEvent, IntegrationEventKey {
    @Override
    public String eventKey() {
        return String.valueOf(returnId.getId());
    }
}
