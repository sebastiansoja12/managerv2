package com.warehouse.supplier.infrastructure.adapter.secondary;

import com.warehouse.commonassets.event.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.supplier.application.port.secondary.SupplierIntegrationEventServicePort;

public class SupplierIntegrationEventServiceAdapter implements SupplierIntegrationEventServicePort {

    private final IntegrationEventPublisher integrationEventPublisher;

    public SupplierIntegrationEventServiceAdapter(final IntegrationEventPublisher integrationEventPublisher) {
        this.integrationEventPublisher = integrationEventPublisher;
    }

    @Override
    public void publishEvent(final IntegrationEvent event) {
        this.integrationEventPublisher.publish(event);
    }
}
