package com.warehouse.commonassets.event.domain.port;

import com.warehouse.commonassets.event.domain.model.IntegrationEvent;

public interface IntegrationEventPublisher {

    void publish(final IntegrationEvent event);
}
