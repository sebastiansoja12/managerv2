package com.warehouse.logisticsdelivery.application.listener;

import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryIntegrationEventServicePort;
import com.warehouse.logisticsdelivery.domain.event.DeliveryChanged;
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
        final DeliverySnapshot delivery = event.getSnapshot();
        if (!delivery.describesCurrentShipmentDelivery()) {
            return;
        }
        this.deliveryIntegrationEventServicePort.publishEvent(new ShipmentDeliveryReadModelChanged(
                new ShipmentDeliveryReadModelData(delivery.shipmentId(), delivery.originDepartmentId(),
                        delivery.destinationDepartmentId(), delivery.plannedAt(), delivery.deliveredAt())));
    }
}
