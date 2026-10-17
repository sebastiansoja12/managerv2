package com.warehouse.shipment.application.listener;

import com.warehouse.shipment.api.event.ShipmentCreatedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentDestinationChangedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentStatusChangedIntegrationEvent;
import com.warehouse.shipment.application.port.secondary.ShipmentEventDataMapperPort;
import com.warehouse.shipment.application.port.secondary.ShipmentIntegrationEventServicePort;
import com.warehouse.shipment.domain.event.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = {"manager.kafka.integration-events.enabled", "manager.kafka.outbox.enabled"},
        havingValue = "true")
public class ShipmentIntegrationEventListener {

    private final ShipmentIntegrationEventServicePort shipmentIntegrationEventServicePort;
    private final ShipmentEventDataMapperPort shipmentEventDataMapper;

    public ShipmentIntegrationEventListener(final ShipmentIntegrationEventServicePort shipmentIntegrationEventServicePort,
                                            final ShipmentEventDataMapperPort shipmentEventDataMapper) {
        this.shipmentIntegrationEventServicePort = shipmentIntegrationEventServicePort;
        this.shipmentEventDataMapper = shipmentEventDataMapper;
    }

    @EventListener
    public void handle(final ShipmentCreated event) {
        shipmentIntegrationEventServicePort.publishEvent(
                new ShipmentCreatedIntegrationEvent(shipmentEventDataMapper.map(event.getSnapshot())));
    }

    @EventListener
    public void handle(final ShipmentDestinationChanged event) {
        shipmentIntegrationEventServicePort.publishEvent(
                new ShipmentDestinationChangedIntegrationEvent(shipmentEventDataMapper.map(event.getSnapshot())));
    }

    @EventListener
    public void handle(final ShipmentStatusChanged event) {
        shipmentIntegrationEventServicePort.publishEvent(
                new ShipmentStatusChangedIntegrationEvent(shipmentEventDataMapper.map(event.getSnapshot())));
    }

    @EventListener
    public void handle(final ShipmentReturned event) {
        shipmentIntegrationEventServicePort.publishEvent(
                new ShipmentStatusChangedIntegrationEvent(shipmentEventDataMapper.map(event.getSnapshot())));
    }

    @EventListener
    public void handle(final ShipmentReturnedCompleted event) {

    }

}
