package com.warehouse.logisticsdelivery.application.listener;

import org.springframework.stereotype.Component;

import com.warehouse.eventoutbox.integration.annotation.IntegrationEventTranslation;
import com.warehouse.logisticsdelivery.api.event.DeliveryAttemptRecordedIntegrationEvent;
import com.warehouse.logisticsdelivery.api.event.DeliveryCanceledIntegrationEvent;
import com.warehouse.logisticsdelivery.api.event.DeliveryCompletedIntegrationEvent;
import com.warehouse.logisticsdelivery.api.event.DeliveryCreatedIntegrationEvent;
import com.warehouse.logisticsdelivery.api.event.DeliveryModifiedIntegrationEvent;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryEventDataMapperPort;
import com.warehouse.logisticsdelivery.domain.event.DeliveryAttemptRecorded;
import com.warehouse.logisticsdelivery.domain.event.DeliveryCanceled;
import com.warehouse.logisticsdelivery.domain.event.DeliveryCompleted;
import com.warehouse.logisticsdelivery.domain.event.DeliveryCreated;
import com.warehouse.logisticsdelivery.domain.event.DeliveryModified;

@Component
public class DeliveryIntegrationEventTranslator {

    private final DeliveryEventDataMapperPort deliveryEventDataMapper;

    public DeliveryIntegrationEventTranslator(final DeliveryEventDataMapperPort deliveryEventDataMapper) {
        this.deliveryEventDataMapper = deliveryEventDataMapper;
    }

    @IntegrationEventTranslation
    public DeliveryCreatedIntegrationEvent translate(final DeliveryCreated event) {
        return new DeliveryCreatedIntegrationEvent(this.deliveryEventDataMapper.map(event.getSnapshot()));
    }

    @IntegrationEventTranslation
    public DeliveryModifiedIntegrationEvent translate(final DeliveryModified event) {
        return new DeliveryModifiedIntegrationEvent(this.deliveryEventDataMapper.map(event.getSnapshot()));
    }

    @IntegrationEventTranslation
    public DeliveryAttemptRecordedIntegrationEvent translate(final DeliveryAttemptRecorded event) {
        return new DeliveryAttemptRecordedIntegrationEvent(this.deliveryEventDataMapper.map(event.getSnapshot()));
    }

    @IntegrationEventTranslation
    public DeliveryCompletedIntegrationEvent translate(final DeliveryCompleted event) {
        return new DeliveryCompletedIntegrationEvent(this.deliveryEventDataMapper.map(event.getSnapshot()));
    }

    @IntegrationEventTranslation
    public DeliveryCanceledIntegrationEvent translate(final DeliveryCanceled event) {
        return new DeliveryCanceledIntegrationEvent(this.deliveryEventDataMapper.map(event.getSnapshot()));
    }
}
