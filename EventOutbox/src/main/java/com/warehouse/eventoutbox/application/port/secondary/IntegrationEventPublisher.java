package com.warehouse.eventoutbox.application.port.secondary;

import com.warehouse.eventoutbox.integration.model.IntegrationEvent;

public interface IntegrationEventPublisher {

    void publish(IntegrationEvent event);
}
