package com.warehouse.logistics.infrastructure.adapter.primary.kafka;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.kafka.infrastructure.adapter.primary.KafkaEventListener;
import com.warehouse.logistics.domain.enumeration.DeliveryDerivationType;
import com.warehouse.logistics.domain.enumeration.DeliveryMethod;
import com.warehouse.logistics.domain.enumeration.PickupMethod;
import com.warehouse.logistics.domain.model.CreateDeliveryCommand;
import com.warehouse.logistics.domain.model.CreateDerivedDeliveryCommand;
import com.warehouse.logistics.domain.port.primary.LogisticsPort;
import com.warehouse.logistics.domain.vo.CompleteDeliveryCommand;
import com.warehouse.shipment.api.event.ShipmentChangedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentCreatedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentDerivedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentDestinationChangedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentStatusChangedIntegrationEvent;
import com.warehouse.shipment.api.event.snapshot.ShipmentDeliveryData;
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
        final ShipmentDeliveryData delivery = event.delivery();
        if (delivery == null) {
            return;
        }
        final ShipmentEventData payload = event.payload();
        final CreateDeliveryCommand command = new CreateDeliveryCommand(
                payload.shipmentId(),
                delivery.originDepartmentId(),
                delivery.destinationDepartmentId(),
                delivery.pickupMethod() == null ? null : PickupMethod.valueOf(delivery.pickupMethod().name()),
                delivery.deliveryMethod() == null ? null : DeliveryMethod.valueOf(delivery.deliveryMethod().name()),
                delivery.pickupPointId(),
                delivery.deliveryPickupPointId(),
                null,
                payload.signatureRequired(),
                event.userId());
        this.operatorContext.runAs(event.operatorId(), event.userId(), event.departmentId(), () -> logisticsPort.createDelivery(command));
    }

    @KafkaEventListener(
            topics = "shipment.derived",
            groupId = "logistics-orchestrator"
    )
    public void handle(final ShipmentDerivedIntegrationEvent event) {
        final CreateDerivedDeliveryCommand command = new CreateDerivedDeliveryCommand(
                event.payload().shipmentId(),
                event.sourceShipmentId(),
                DeliveryDerivationType.valueOf(event.derivationType().name()),
                event.payload().signatureRequired(),
                event.userId());
        this.operatorContext.runAs(event.operatorId(), event.userId(), event.departmentId(),
                () -> logisticsPort.createDerivedDelivery(command));
    }

    @KafkaEventListener(
            topics = "shipment.events",
            groupId = "logistics-orchestrator"
    )
    public void handle(final ShipmentChangedIntegrationEvent event) {
        if (event instanceof ShipmentDestinationChangedIntegrationEvent destinationChangedEvent) {
            this.operatorContext.runAs(event.operatorId(), event.userId(), event.departmentId(),
                    () -> logisticsPort.changeDestinationDepartment(event.payload().shipmentId(),
                            destinationChangedEvent.destinationDepartmentId()));
            return;
        }
        final ShipmentEventData payload = event.payload();
        if (event instanceof ShipmentStatusChangedIntegrationEvent && payload.shipmentStatus() == ShipmentStatus.DELIVERY) {
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
