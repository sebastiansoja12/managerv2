package com.warehouse.logistics.domain.port.primary;

import com.warehouse.logistics.domain.enumeration.DeliveryType;
import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.logistics.domain.enumeration.DeliveryMethod;
import com.warehouse.logistics.domain.model.CreateDeliveryCommand;
import com.warehouse.logistics.domain.model.Delivery;
import com.warehouse.logistics.domain.model.DeliveryTarget;
import com.warehouse.logistics.domain.model.LogisticsRequest;
import com.warehouse.logistics.domain.model.LogisticsResponse;
import com.warehouse.logistics.domain.service.LogisticsService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

public class LogisticsPortImpl implements LogisticsPort {

    private final LogisticsService logisticsService;

    public LogisticsPortImpl(final LogisticsService logisticsService) {
        this.logisticsService = logisticsService;
    }

    @Override
    public Set<LogisticsResponse> processDelivery(final Set<LogisticsRequest> logisticsRequests) {
        return this.logisticsService.save(logisticsRequests);
    }

    @Override
    public void createDelivery(final CreateDeliveryCommand command) {
        final DeliveryTarget target = DeliveryTarget.shipment(command.shipmentId());
        if (logisticsService.findByTargetAndType(target, DeliveryType.OUTBOUND).isEmpty()) {
            logisticsService.createOrUpdate(new Delivery(target, command.shipmentId(), DeliveryType.OUTBOUND, command));
        }
    }

    @Override
    public List<Delivery> findRecentDeliveries(final int offset, final int limit) {
        return logisticsService.findRecent(offset, limit);
    }

    @Override
    public Delivery findDelivery(final DeliveryId deliveryId) {
        return logisticsService.findById(deliveryId)
                .orElseThrow(() -> new NoSuchElementException("Delivery not found"));
    }

    @Override
    public Delivery changeDeliveryMethod(final DeliveryId deliveryId, final DeliveryMethod method) {
        final Delivery delivery = findDelivery(deliveryId);
        delivery.changeMethod(method);
        logisticsService.createOrUpdate(delivery);
        return delivery;
    }

    @Override
    public void cancelDelivery(final DeliveryId deliveryId) {
        final Delivery delivery = findDelivery(deliveryId);
        delivery.markAsCanceled();
        logisticsService.createOrUpdate(delivery);
    }
}
