package com.warehouse.shipment.infrastructure.adapter.primary.kafka;

import com.warehouse.commonassets.kafka.infrastructure.adapter.primary.KafkaEventListener;
import com.warehouse.shipment.api.event.ShipmentReadModelChanged;
import com.warehouse.shipment.application.port.primary.ShipmentReadModelSyncPort;
import org.springframework.stereotype.Component;

@Component
public class ShipmentIntegrationListener {
    private final ShipmentReadModelSyncPort shipmentReadModelSyncPort;

    public ShipmentIntegrationListener(final ShipmentReadModelSyncPort shipmentReadModelSyncPort) {
        this.shipmentReadModelSyncPort = shipmentReadModelSyncPort;
    }

    @KafkaEventListener(topics = "${manager.kafka.topics.shipment-read-model-sync:shipment.read-model.sync}",
            groupId = "shipment-management")
    public void handle(final ShipmentReadModelChanged event) {
        shipmentReadModelSyncPort.syncReadModel(event.snapshot().shipmentId());
    }
}
