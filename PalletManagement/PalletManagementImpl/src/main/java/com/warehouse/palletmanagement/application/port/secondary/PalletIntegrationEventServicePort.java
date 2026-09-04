package com.warehouse.palletmanagement.application.port.secondary;

import com.warehouse.commonassets.event.integration.model.IntegrationEvent;

public interface PalletIntegrationEventServicePort {
    void publishEvent(final IntegrationEvent event);
}
