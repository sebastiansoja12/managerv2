package com.warehouse.supplier.application.port.secondary;

import com.warehouse.commonassets.event.integration.model.IntegrationEvent;

public interface SupplierIntegrationEventServicePort {

    void publishEvent(final IntegrationEvent event);
}
