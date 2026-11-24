package com.warehouse.logisticsdelivery.application.service;

import com.warehouse.eventoutbox.application.port.secondary.DomainEventPublisher;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryRepository;
import com.warehouse.logisticsdelivery.domain.event.DeliveryCanceled;
import com.warehouse.logisticsdelivery.domain.event.DeliveryChanged;
import com.warehouse.logisticsdelivery.domain.model.Delivery;
import com.warehouse.logisticsdelivery.domain.vo.DeliverySnapshot;

import java.time.Instant;
import java.util.function.BiFunction;

public class DeliveryChangeService {

    private final DeliveryRepository deliveryRepository;

    private final DomainEventPublisher domainEventPublisher;

    public DeliveryChangeService(final DeliveryRepository deliveryRepository,
                                 final DomainEventPublisher domainEventPublisher) {
        this.deliveryRepository = deliveryRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    public DeliverySnapshot save(final Delivery delivery,
                                 final BiFunction<DeliverySnapshot, Instant, DeliveryChanged> event) {
        this.deliveryRepository.createOrUpdate(delivery);
        final DeliverySnapshot snapshot = delivery.snapshot();
        this.domainEventPublisher.publish(event.apply(snapshot, Instant.now()));
        return snapshot;
    }

    public DeliverySnapshot saveChanged(final Delivery delivery, final DeliverySnapshot previousSnapshot,
                                        final BiFunction<DeliverySnapshot, Instant, DeliveryChanged> event) {
        if (previousSnapshot.equals(delivery.snapshot())) {
            return previousSnapshot;
        }
        return save(delivery, event);
    }

    public void cancel(final Delivery delivery) {
        delivery.markAsCanceled();
        save(delivery, DeliveryCanceled::new);
    }
}
