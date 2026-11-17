package com.warehouse.logisticsdelivery.application.port.primary;

import com.warehouse.commonassets.event.application.port.secondary.DomainEventPublisher;
import com.warehouse.logisticsdelivery.application.port.primary.command.SearchDeliveriesCommand;
import com.warehouse.logisticsdelivery.application.port.primary.result.DeliveryReadPage;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryReadModelRepository;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryRepository;
import com.warehouse.logisticsdelivery.domain.event.DeliveryReadModelRebuilt;
import com.warehouse.logisticsdelivery.domain.model.Delivery;
import com.warehouse.logisticsdelivery.domain.vo.DeliverySnapshot;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class DeliveryReadModelPortImpl implements DeliveryReadModelPort {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryReadModelRepository deliveryReadModelRepository;
    private final DomainEventPublisher domainEventPublisher;

    public DeliveryReadModelPortImpl(final DeliveryRepository deliveryRepository,
                                     final DeliveryReadModelRepository deliveryReadModelRepository,
                                     final DomainEventPublisher domainEventPublisher) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryReadModelRepository = deliveryReadModelRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    @Transactional
    public void sync(final DeliverySnapshot delivery) {
        this.deliveryReadModelRepository.sync(delivery);
    }

    @Override
    @Transactional
    public int syncReadModels(final LocalDate dateFrom, final LocalDate dateTo) {
        final List<Delivery> deliveries = this.deliveryRepository.findAllCreatedBetween(
                dateFrom.atStartOfDay(),
                dateTo.plusDays(1).atStartOfDay());
        deliveries.stream()
                .map(Delivery::snapshot)
                .forEach(delivery -> {
                    this.deliveryReadModelRepository.sync(delivery);
                    this.domainEventPublisher.publish(new DeliveryReadModelRebuilt(delivery, Instant.now()));
                });
        return deliveries.size();
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryReadPage search(final SearchDeliveriesCommand command) {
        return this.deliveryReadModelRepository.search(command);
    }
}
