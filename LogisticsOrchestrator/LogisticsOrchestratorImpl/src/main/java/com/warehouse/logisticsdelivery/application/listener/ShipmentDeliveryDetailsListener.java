package com.warehouse.logisticsdelivery.application.listener;

import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryIntegrationEventServicePort;
import com.warehouse.logisticsdelivery.domain.event.DeliveryChanged;
import com.warehouse.logisticsdelivery.domain.event.DeliveryReadModelRebuilt;
import com.warehouse.logisticsdelivery.domain.vo.DeliverySnapshot;
import com.warehouse.shipment.api.event.ShipmentDeliveryReadModelChanged;
import com.warehouse.shipment.api.event.snapshot.ShipmentDeliveryReadModelData;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = {"manager.kafka.integration-events.enabled", "manager.kafka.outbox.enabled"},
        havingValue = "true")
public class ShipmentDeliveryDetailsListener {

    private final DeliveryIntegrationEventServicePort deliveryIntegrationEventServicePort;

    public ShipmentDeliveryDetailsListener(final DeliveryIntegrationEventServicePort deliveryIntegrationEventServicePort) {
        this.deliveryIntegrationEventServicePort = deliveryIntegrationEventServicePort;
    }

    @EventListener
    public void handle(final DeliveryChanged event) {
        publishShipmentDeliveryDetails(event.getSnapshot());
    }

    @EventListener
    public void handle(final DeliveryReadModelRebuilt event) {
        publishShipmentDeliveryDetails(event.getSnapshot());
    }

    private void publishShipmentDeliveryDetails(final DeliverySnapshot delivery) {
        if (!delivery.describesCurrentShipmentDelivery()) {
            return;
        }
        this.deliveryIntegrationEventServicePort.publishEvent(new ShipmentDeliveryReadModelChanged(
                new ShipmentDeliveryReadModelData(delivery.shipmentId(), delivery.originDepartmentId(),
                        delivery.destinationDepartmentId(), delivery.currentDepartmentId(), delivery.plannedAt(),
                        delivery.deliveredAt())));
    }
}
