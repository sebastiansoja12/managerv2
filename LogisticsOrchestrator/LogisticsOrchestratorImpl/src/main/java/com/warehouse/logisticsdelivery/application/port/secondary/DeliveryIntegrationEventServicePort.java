package com.warehouse.logisticsdelivery.application.port.secondary;

import com.warehouse.commonassets.event.integration.model.IntegrationEvent;

public interface DeliveryIntegrationEventServicePort {

    void publishEvent(final IntegrationEvent event);
}
