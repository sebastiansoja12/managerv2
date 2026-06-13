package com.warehouse.commonassets.event.application.port.secondary;

import com.warehouse.commonassets.event.integration.model.IntegrationEvent;

public interface IntegrationEventPublisher {

    void publish(IntegrationEvent event);
}
