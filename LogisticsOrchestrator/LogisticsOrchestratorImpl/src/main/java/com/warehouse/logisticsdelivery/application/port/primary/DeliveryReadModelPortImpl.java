package com.warehouse.logisticsdelivery.application.port.primary;

import com.warehouse.logisticsdelivery.application.port.primary.command.SearchDeliveriesCommand;
import com.warehouse.logisticsdelivery.application.port.primary.result.DeliveryReadPage;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryReadModelRepository;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryRepository;
import com.warehouse.logisticsdelivery.domain.model.Delivery;
import com.warehouse.logisticsdelivery.domain.vo.DeliverySnapshot;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public class DeliveryReadModelPortImpl implements DeliveryReadModelPort {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryReadModelRepository deliveryReadModelRepository;

    public DeliveryReadModelPortImpl(final DeliveryRepository deliveryRepository,
                                     final DeliveryReadModelRepository deliveryReadModelRepository) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryReadModelRepository = deliveryReadModelRepository;
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
                .forEach(this.deliveryReadModelRepository::sync);
        return deliveries.size();
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryReadPage search(final SearchDeliveriesCommand command) {
        return this.deliveryReadModelRepository.search(command);
    }
}
