package com.warehouse.shipment.application.port.secondary;

import com.warehouse.commonassets.event.integration.model.IntegrationEvent;

public interface ShipmentIntegrationEventServicePort {

    void publishEvent(final IntegrationEvent event);
}
