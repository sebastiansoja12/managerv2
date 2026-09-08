package com.warehouse.logisticsdelivery.infrastructure.adapter.primary.kafka;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.kafka.infrastructure.adapter.primary.KafkaEventListener;
import com.warehouse.logisticsdelivery.application.port.primary.DeliveryPort;
import com.warehouse.logisticsdelivery.application.port.primary.command.CompleteDeliveryCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.CreateDeliveryCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.CreateDerivedDeliveryCommand;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryMethod;
import com.warehouse.logisticsdelivery.domain.enumeration.PickupMethod;
import com.warehouse.shipment.api.event.ShipmentChangedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentCreatedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentDestinationChangedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentStatusChangedIntegrationEvent;
import com.warehouse.shipment.api.event.snapshot.ShipmentDeliveryData;
import com.warehouse.shipment.api.event.snapshot.ShipmentEventData;
import org.springframework.stereotype.Component;

@Component
public class ShipmentDeliveryEventListener {

    private final DeliveryPort deliveryPort;
    private final OperatorContext operatorContext;

    public ShipmentDeliveryEventListener(final DeliveryPort deliveryPort,
                                         final OperatorContext operatorContext) {
        this.deliveryPort = deliveryPort;
        this.operatorContext = operatorContext;
    }

    @KafkaEventListener(
            topics = "shipment.created",
            groupId = "logistics-orchestrator"
    )
    public void handle(final ShipmentCreatedIntegrationEvent event) {
        final ShipmentDeliveryData delivery = event.delivery();
        final ShipmentEventData payload = event.payload();
        if (delivery == null) {
            final CreateDerivedDeliveryCommand command = new CreateDerivedDeliveryCommand(payload.shipmentId(),
                    payload.signatureRequired(), event.userId());
            this.operatorContext.runAs(event.operatorId(), event.userId(), event.departmentId(),
                    () -> deliveryPort.createDerivedDelivery(command));
            return;
        }
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
        this.operatorContext.runAs(event.operatorId(), event.userId(), event.departmentId(), () -> deliveryPort.createDelivery(command));
    }

    @KafkaEventListener(
            topics = "shipment.events",
            groupId = "logistics-orchestrator"
    )
    public void handle(final ShipmentChangedIntegrationEvent event) {
        if (event instanceof ShipmentDestinationChangedIntegrationEvent destinationChangedEvent) {
            this.operatorContext.runAs(event.operatorId(), event.userId(), event.departmentId(),
                    () -> deliveryPort.changeDestinationDepartment(event.payload().shipmentId(),
                            destinationChangedEvent.destinationDepartmentId()));
            return;
        }
        final ShipmentEventData payload = event.payload();
        if (event instanceof ShipmentStatusChangedIntegrationEvent && payload.shipmentStatus() == ShipmentStatus.DELIVERY) {
            final CompleteDeliveryCommand command = new CompleteDeliveryCommand(payload.shipmentId(), null,
                    event.userId(), event.departmentId());
            this.operatorContext.runAs(event.operatorId(), event.userId(), event.departmentId(), () -> deliveryPort.completeDelivery(command));
        }
    }
}
