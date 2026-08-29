package com.warehouse.shipment.application.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.commonassets.event.domain.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.domain.model.IntegrationEvent;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.kafka.domain.model.OperatorAwareContext;

@IntegrationEventType(value = "shipment.return.canceled", version = 1)
public class ShipmentReturnCanceledIntegrationEvent extends OperatorAwareContext implements IntegrationEvent {

    private ShipmentId shipmentId;

    @JsonCreator
    public ShipmentReturnCanceledIntegrationEvent(@JsonProperty("shipmentId") final ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }
}
