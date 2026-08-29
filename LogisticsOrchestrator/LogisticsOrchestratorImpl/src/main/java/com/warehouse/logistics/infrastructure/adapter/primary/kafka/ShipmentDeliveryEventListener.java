package com.warehouse.logistics.infrastructure.adapter.primary.kafka;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.kafka.infrastructure.adapter.primary.KafkaEventListener;
import com.warehouse.logistics.domain.enumeration.DeliveryMethod;
import com.warehouse.logistics.domain.model.CreateDeliveryCommand;
import com.warehouse.logistics.domain.port.primary.LogisticsPort;
import com.warehouse.logistics.domain.vo.CompleteDeliveryCommand;
import com.warehouse.shipment.api.event.ShipmentCreatedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentStatusChangedIntegrationEvent;
import com.warehouse.shipment.api.event.snapshot.ShipmentEventData;
import org.springframework.stereotype.Component;

@Component
public class ShipmentDeliveryEventListener {

    private final LogisticsPort logisticsPort;
    private final OperatorContext operatorContext;

    public ShipmentDeliveryEventListener(final LogisticsPort logisticsPort,
                                         final OperatorContext operatorContext) {
        this.logisticsPort = logisticsPort;
        this.operatorContext = operatorContext;
    }

    @KafkaEventListener(
            topics = "shipment.created",
            groupId = "logistics-orchestrator"
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
        this.operatorContext.runAs(event.operatorId(), event.userId(), event.departmentId(), () -> logisticsPort.createDelivery(command));
    }

    @KafkaEventListener(
            topics = "shipment.events",
            groupId = "logistics-orchestrator"
    )
    public void handle(final ShipmentStatusChangedIntegrationEvent event) {
        final ShipmentEventData payload = event.payload();
        if (payload.shipmentStatus() == ShipmentStatus.DELIVERY) {
            final CompleteDeliveryCommand command = CompleteDeliveryCommand.builder()
                    .departmentId(event.departmentId())
                    .shipmentId(payload.shipmentId())
                    .userId(event.userId())
                    .supplierId(null)
                    .build();
            this.operatorContext.runAs(event.operatorId(), event.userId(), event.departmentId(), () -> logisticsPort.completeDelivery(command));
        }
    }
}
