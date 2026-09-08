package com.warehouse.logisticsdelivery.application.listener;

import com.warehouse.logisticsdelivery.api.event.DeliveryAttemptRecordedIntegrationEvent;
import com.warehouse.logisticsdelivery.api.event.DeliveryCanceledIntegrationEvent;
import com.warehouse.logisticsdelivery.api.event.DeliveryCompletedIntegrationEvent;
import com.warehouse.logisticsdelivery.api.event.DeliveryCreatedIntegrationEvent;
import com.warehouse.logisticsdelivery.api.event.DeliveryModifiedIntegrationEvent;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryEventDataMapperPort;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryIntegrationEventServicePort;
import com.warehouse.logisticsdelivery.domain.event.DeliveryAttemptRecorded;
import com.warehouse.logisticsdelivery.domain.event.DeliveryCanceled;
import com.warehouse.logisticsdelivery.domain.event.DeliveryCompleted;
import com.warehouse.logisticsdelivery.domain.event.DeliveryCreated;
import com.warehouse.logisticsdelivery.domain.event.DeliveryModified;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = {"manager.kafka.integration-events.enabled", "manager.kafka.outbox.enabled"},
        havingValue = "true")
public class DeliveryIntegrationEventListener {

    private final DeliveryIntegrationEventServicePort deliveryIntegrationEventServicePort;
    private final DeliveryEventDataMapperPort deliveryEventDataMapper;

    public DeliveryIntegrationEventListener(final DeliveryIntegrationEventServicePort deliveryIntegrationEventServicePort,
                                            final DeliveryEventDataMapperPort deliveryEventDataMapper) {
        this.deliveryIntegrationEventServicePort = deliveryIntegrationEventServicePort;
        this.deliveryEventDataMapper = deliveryEventDataMapper;
    }

    @EventListener
    public void handle(final DeliveryCreated event) {
        this.deliveryIntegrationEventServicePort.publishEvent(
                new DeliveryCreatedIntegrationEvent(this.deliveryEventDataMapper.map(event.getSnapshot())));
    }

    @EventListener
    public void handle(final DeliveryModified event) {
        this.deliveryIntegrationEventServicePort.publishEvent(
                new DeliveryModifiedIntegrationEvent(this.deliveryEventDataMapper.map(event.getSnapshot())));
    }

    @EventListener
    public void handle(final DeliveryAttemptRecorded event) {
        this.deliveryIntegrationEventServicePort.publishEvent(
                new DeliveryAttemptRecordedIntegrationEvent(this.deliveryEventDataMapper.map(event.getSnapshot())));
    }

    @EventListener
    public void handle(final DeliveryCompleted event) {
        this.deliveryIntegrationEventServicePort.publishEvent(
                new DeliveryCompletedIntegrationEvent(this.deliveryEventDataMapper.map(event.getSnapshot())));
    }

    @EventListener
    public void handle(final DeliveryCanceled event) {
        this.deliveryIntegrationEventServicePort.publishEvent(
                new DeliveryCanceledIntegrationEvent(this.deliveryEventDataMapper.map(event.getSnapshot())));
    }
}
