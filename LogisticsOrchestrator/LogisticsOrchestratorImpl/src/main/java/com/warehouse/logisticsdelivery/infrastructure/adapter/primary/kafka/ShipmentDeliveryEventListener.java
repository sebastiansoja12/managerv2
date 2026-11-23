package com.warehouse.logisticsdelivery.infrastructure.adapter.primary.kafka;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventListener;
import com.warehouse.logisticsdelivery.application.port.primary.DeliveryPort;
import com.warehouse.logisticsdelivery.application.port.primary.command.CompleteDeliveryCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.CreateDeliveryCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.CreateDerivedDeliveryCommand;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryMethod;
import com.warehouse.logisticsdelivery.domain.enumeration.PickupMethod;
import com.warehouse.logisticsdelivery.domain.vo.RecipientAddress;
import com.warehouse.shipment.api.event.ShipmentCanceledIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentCreatedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentDestinationChangedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentStatusChangedIntegrationEvent;
import com.warehouse.shipment.api.event.snapshot.ShipmentDeliveryData;
import com.warehouse.shipment.api.event.snapshot.PartySnapshot;
import com.warehouse.shipment.api.event.snapshot.ShipmentEventData;
import org.springframework.stereotype.Component;

@Component
public class ShipmentDeliveryEventListener {

    private final DeliveryPort deliveryPort;

    public ShipmentDeliveryEventListener(final DeliveryPort deliveryPort) {
        this.deliveryPort = deliveryPort;
    }

    @IntegrationEventListener(group = "logistics-orchestrator")
    public void handle(final ShipmentCreatedIntegrationEvent event) {
        final ShipmentDeliveryData delivery = event.delivery();
        final ShipmentEventData payload = event.payload();
        if (delivery == null) {
            final CreateDerivedDeliveryCommand command = new CreateDerivedDeliveryCommand(payload.shipmentId(),
                    payload.signatureRequired(), event.userId());
            deliveryPort.createDerivedDelivery(command);
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
        deliveryPort.createDelivery(command);
    }

    @IntegrationEventListener(group = "logistics-orchestrator")
    public void handle(final ShipmentCanceledIntegrationEvent event) {
        deliveryPort.cancelShipmentDeliveries(event.payload().shipmentId());
    }

    @IntegrationEventListener(group = "logistics-orchestrator")
    public void handle(final ShipmentDestinationChangedIntegrationEvent event) {
        final PartySnapshot recipient = event.payload().recipient();
        if (recipient != null) {
            deliveryPort.changeRecipientAddress(event.payload().shipmentId(), new RecipientAddress(
                    recipient.city(), recipient.street(), recipient.postalCode(), recipient.countryCode()));
        }
    }

    @IntegrationEventListener(group = "logistics-orchestrator")
    public void handle(final ShipmentStatusChangedIntegrationEvent event) {
        final ShipmentEventData payload = event.payload();
        if (payload.shipmentStatus() == ShipmentStatus.LOST) {
            deliveryPort.cancelShipmentDeliveries(payload.shipmentId());
            return;
        }
        if (payload.shipmentStatus() == ShipmentStatus.DELIVERY) {
            final CompleteDeliveryCommand command = new CompleteDeliveryCommand(payload.shipmentId(), null,
                    event.userId(), event.departmentId());
            deliveryPort.completeDelivery(command);
        }
    }
}
