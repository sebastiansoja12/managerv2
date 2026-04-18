package com.warehouse.auth;

import com.warehouse.auth.event.OperatorCreatedEvent;

public interface OperatorUserEventPublisher {
    void sendEvent(final OperatorCreatedEvent event);
}
