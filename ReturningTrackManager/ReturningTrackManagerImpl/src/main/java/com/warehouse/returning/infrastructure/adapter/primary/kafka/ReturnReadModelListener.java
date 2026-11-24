package com.warehouse.returning.infrastructure.adapter.primary.kafka;

import com.warehouse.eventoutbox.integration.annotation.IntegrationEventListener;
import com.warehouse.returning.api.event.ReturnReadModelChangedIntegrationEvent;
import com.warehouse.returning.application.port.primary.ReturnReadModelSyncPort;
import org.springframework.stereotype.Component;

@Component
public class ReturnReadModelListener {

    private final ReturnReadModelSyncPort returnReadModelSyncPort;

    public ReturnReadModelListener(final ReturnReadModelSyncPort returnReadModelSyncPort) {
        this.returnReadModelSyncPort = returnReadModelSyncPort;
    }

    @IntegrationEventListener(group = "returning-track-manager-read-model")
    public void handle(final ReturnReadModelChangedIntegrationEvent event) {
        returnReadModelSyncPort.syncReadModel(event.returnPackageId());
    }
}
