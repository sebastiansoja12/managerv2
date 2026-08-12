package com.warehouse.returning.application.port.secondary;

import com.warehouse.returning.api.event.ReturnLifecycleIntegrationEvent;

public interface ReturnEventPublisherServicePort {
    void publish(final ReturnLifecycleIntegrationEvent event);
}
