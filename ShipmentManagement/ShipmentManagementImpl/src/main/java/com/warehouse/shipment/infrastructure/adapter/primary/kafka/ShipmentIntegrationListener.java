package com.warehouse.shipment.infrastructure.adapter.primary.kafka;

import com.warehouse.commonassets.kafka.infrastructure.adapter.primary.KafkaEventListener;
import com.warehouse.shipment.api.event.ShipmentDeliveryReadModelChanged;
import com.warehouse.shipment.api.event.ShipmentReadModelChanged;
import com.warehouse.shipment.api.event.snapshot.ShipmentDeliveryReadModelData;
import com.warehouse.shipment.application.port.primary.ShipmentReadModelSyncPort;
import com.warehouse.shipment.domain.vo.ShipmentDeliveryDetails;
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

    @KafkaEventListener(
            topics = "${manager.kafka.topics.shipment-delivery-read-model-sync:shipment.read-model.delivery-sync}",
            groupId = "shipment-management-delivery")
    public void handle(final ShipmentDeliveryReadModelChanged event) {
        final ShipmentDeliveryReadModelData payload = event.payload();
        shipmentReadModelSyncPort.syncReadModel(payload.shipmentId(), new ShipmentDeliveryDetails(
                payload.originDepartmentId(), payload.destinationDepartmentId(), payload.plannedDeliveryAt(),
                payload.deliveredAt()));
    }
}
