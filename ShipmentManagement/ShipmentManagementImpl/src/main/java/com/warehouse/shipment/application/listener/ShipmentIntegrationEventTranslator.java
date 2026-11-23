package com.warehouse.shipment.application.listener;

import java.util.List;

import org.springframework.stereotype.Component;

import com.warehouse.eventoutbox.integration.annotation.IntegrationEventTranslation;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentCanceledIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentCreatedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentDeliveredIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentDestinationChangedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentStatusChangedIntegrationEvent;
import com.warehouse.shipment.application.port.secondary.ShipmentEventDataMapperPort;
import com.warehouse.shipment.domain.event.ShipmentCanceled;
import com.warehouse.shipment.domain.event.ShipmentCreated;
import com.warehouse.shipment.domain.event.ShipmentDelivered;
import com.warehouse.shipment.domain.event.ShipmentDestinationChanged;
import com.warehouse.shipment.domain.event.ShipmentStatusChanged;

@Component
public class ShipmentIntegrationEventTranslator {

    private final ShipmentEventDataMapperPort shipmentEventDataMapper;

    public ShipmentIntegrationEventTranslator(final ShipmentEventDataMapperPort shipmentEventDataMapper) {
        this.shipmentEventDataMapper = shipmentEventDataMapper;
    }

    @IntegrationEventTranslation
    public ShipmentCreatedIntegrationEvent translate(final ShipmentCreated event) {
        return new ShipmentCreatedIntegrationEvent(shipmentEventDataMapper.map(event.getSnapshot()),
                event.getDeliveryPlan().map(shipmentEventDataMapper::map).orElse(null));
    }

    @IntegrationEventTranslation
    public ShipmentStatusChangedIntegrationEvent translate(final ShipmentStatusChanged event) {
        return new ShipmentStatusChangedIntegrationEvent(shipmentEventDataMapper.map(event.getSnapshot()));
    }

    @IntegrationEventTranslation
    public List<IntegrationEvent> translate(final ShipmentDestinationChanged event) {
        return List.of(
                new ShipmentStatusChangedIntegrationEvent(shipmentEventDataMapper.map(event.getSnapshot())),
                new ShipmentDestinationChangedIntegrationEvent(shipmentEventDataMapper.map(event.getSnapshot())));
    }

    @IntegrationEventTranslation
    public List<IntegrationEvent> translate(final ShipmentCanceled event) {
        return List.of(
                new ShipmentStatusChangedIntegrationEvent(shipmentEventDataMapper.map(event.getSnapshot())),
                new ShipmentCanceledIntegrationEvent(shipmentEventDataMapper.map(event.getSnapshot())));
    }

    @IntegrationEventTranslation
    public List<IntegrationEvent> translate(final ShipmentDelivered event) {
        return List.of(
                new ShipmentStatusChangedIntegrationEvent(shipmentEventDataMapper.map(event.getSnapshot())),
                new ShipmentDeliveredIntegrationEvent(shipmentEventDataMapper.map(event.getSnapshot())));
    }
}
