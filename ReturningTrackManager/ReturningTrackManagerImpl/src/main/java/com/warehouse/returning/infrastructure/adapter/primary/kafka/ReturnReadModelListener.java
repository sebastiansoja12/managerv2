package com.warehouse.returning.infrastructure.adapter.primary.kafka;

import com.warehouse.returning.api.event.ReturnReadModelChangedIntegrationEvent;
import com.warehouse.returning.application.port.primary.ReturnReadModelSyncPort;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ReturnReadModelListener {

    private final ReturnReadModelSyncPort returnReadModelSyncPort;

    public ReturnReadModelListener(final ReturnReadModelSyncPort returnReadModelSyncPort) {
        this.returnReadModelSyncPort = returnReadModelSyncPort;
    }

    @KafkaListener(topics = "${manager.kafka.topics.return-read-model-sync:return.read-model.sync}",
            groupId = "returning-track-manager-read-model")
    public void handle(final ReturnReadModelChangedIntegrationEvent event) {
        returnReadModelSyncPort.syncReadModel(event.returnPackageId());
    }
}
