package com.warehouse.logistics.domain.port.primary;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.logistics.domain.enumeration.DeliveryMethod;
import com.warehouse.logistics.domain.enumeration.DeliveryType;
import com.warehouse.logistics.domain.model.*;
import com.warehouse.logistics.domain.service.LogisticsService;
import com.warehouse.logistics.domain.vo.CompleteDeliveryCommand;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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
        final Optional<Delivery> possibleExistingDelivery = this.logisticsService.findByTargetAndType(target, DeliveryType.OUTBOUND);
        if (possibleExistingDelivery.isPresent()) {
            throw new RuntimeException("Delivery for shipment: " + command.shipmentId().toString() + " already exists");
        }

		final Delivery delivery = new Delivery(target, command.shipmentId(), DeliveryType.OUTBOUND, command.method(),
				command.pickupPointId(), command.deliveryPickupPointId(), command.signatureId(),
				command.signatureRequired(), command.userId());

		this.logisticsService.createOrUpdate(delivery);
    }

    @Override
    public void completeDelivery(final CompleteDeliveryCommand command) {
        final DeliveryTarget target = DeliveryTarget.shipment(command.getShipmentId());
        final Optional<Delivery> delivery = this.logisticsService.findByTargetAndType(target, DeliveryType.OUTBOUND);
        delivery.ifPresent(
                d -> {
                    final DeliveryStep finalStep = DeliveryStep.complete(d.getDeliveryId(), command.getSupplierId(), command.getUserId(),
                            command.getDepartmentId(), d.getDeliverySteps().size());
                    d.addStep(finalStep);
                    d.markAsCompleted();
                    this.logisticsService.createOrUpdate(d);
                }
        );
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
    public Delivery findDeliveryByShipmentId(final ShipmentId shipmentId) {
        return logisticsService.findByTargetAndType(DeliveryTarget.shipment(shipmentId), DeliveryType.OUTBOUND)
                .orElseThrow(() -> new NoSuchElementException("Delivery not found for shipment"));
    }

    @Override
    public void changeDeliveryMethod(final DeliveryId deliveryId, final DeliveryMethod method) {
        final Delivery delivery = findDelivery(deliveryId);
        delivery.changeMethod(method);
        this.logisticsService.createOrUpdate(delivery);
    }

    @Override
    public void cancelDelivery(final DeliveryId deliveryId) {
        final Delivery delivery = findDelivery(deliveryId);
        delivery.markAsCanceled();
        logisticsService.createOrUpdate(delivery);
    }
}
