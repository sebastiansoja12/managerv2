package com.warehouse.logisticsdelivery.application.port.secondary;

import com.warehouse.logisticsdelivery.api.event.DeliveryChangedIntegrationEvent;

public interface DeliveryIntegrationEventServicePort {

    void publishEvent(final DeliveryChangedIntegrationEvent event);
}
