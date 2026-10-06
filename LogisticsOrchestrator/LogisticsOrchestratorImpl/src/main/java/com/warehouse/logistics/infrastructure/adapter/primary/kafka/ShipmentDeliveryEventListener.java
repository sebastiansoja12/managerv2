package com.warehouse.logistics.infrastructure.adapter.primary.kafka;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.kafka.infrastructure.adapter.primary.KafkaEventListener;
import com.warehouse.logistics.domain.enumeration.DeliveryMethod;
import com.warehouse.logistics.domain.model.CreateDeliveryCommand;
import com.warehouse.logistics.domain.port.primary.LogisticsPort;
import com.warehouse.shipment.api.event.ShipmentCreatedIntegrationEvent;
import com.warehouse.shipment.api.event.snapshot.ShipmentEventData;
import org.springframework.stereotype.Component;

@Component
public class ShipmentDeliveryEventListener {

    private final LogisticsPort logisticsPort;

    public ShipmentDeliveryEventListener(final LogisticsPort logisticsPort) {
        this.logisticsPort = logisticsPort;
    }

    @KafkaEventListener(
            topics = "${manager.kafka.topics.shipment-created:shipment.created}",
            groupId = "${manager.kafka.groups.logistics-orchestrator:logistics-orchestrator}"
    )
    public void handle(final ShipmentCreatedIntegrationEvent event) {
        final ShipmentEventData payload = event.payload();
        final CreateDeliveryCommand command = new CreateDeliveryCommand(
                payload.shipmentId(),
                DeliveryMethod.valueOf(payload.deliveryMethod().name()),
                payload.pickupPointId(),
                payload.deliveryPickupPointId(),
                null,
                payload.signatureRequired(),
                event.userId());
        new OperatorContext().runAs(event.operatorId(), event.userId(), () -> logisticsPort.createDelivery(command));
    }
}
