package com.warehouse.auth;

import com.warehouse.auth.event.SupplierCreatedEvent;

public interface SupplierUserEventPublisher {
    void sendEvent(final SupplierCreatedEvent event);
}
