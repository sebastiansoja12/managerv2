package com.warehouse.logistics.domain.port.primary;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.logistics.domain.enumeration.DeliveryMethod;
import com.warehouse.logistics.domain.enumeration.DeliveryType;
import com.warehouse.logistics.domain.model.*;
import com.warehouse.logistics.domain.service.LogisticsService;
import com.warehouse.logistics.domain.vo.CompleteDeliveryCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

public class LogisticsPortImpl implements LogisticsPort {

    private static final Logger log = LoggerFactory.getLogger(LogisticsPortImpl.class);

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
    @Transactional
    public void createPalletDelivery(final CreatePalletDeliveryCommand command) {
        final DeliveryTarget target = DeliveryTarget.pallet(command.palletId());
        final Delivery palletDelivery = this.logisticsService.findByTargetAndType(target, DeliveryType.OUTBOUND)
                .orElseGet(() -> {
                    final Delivery delivery = Delivery.forPallet(target, command.userId());
                    this.logisticsService.createOrUpdate(delivery);
                    log.info("Pallet delivery {} created for pallet {}", delivery.getDeliveryId().getId(),
                            command.palletId());
                    return delivery;
                });
        for (final ShipmentId shipmentId : command.shipmentIds()) {
            this.logisticsService.findByTargetAndType(DeliveryTarget.shipment(shipmentId), DeliveryType.OUTBOUND)
                    .filter(delivery -> delivery.replaceByPalletDelivery(palletDelivery.getDeliveryId(),
                            command.userId()))
                    .ifPresent(this.logisticsService::createOrUpdate);
        }
    }

    @Override
    public void completeDelivery(final CompleteDeliveryCommand command) {
        final DeliveryTarget target = DeliveryTarget.shipment(command.getShipmentId());
        final Optional<Delivery> delivery = this.logisticsService.findByTargetAndType(target, DeliveryType.OUTBOUND)
                .filter(Delivery::isActive);
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
    public Delivery findDeliveryByPalletId(final String palletId) {
        return logisticsService.findByTargetAndType(DeliveryTarget.pallet(palletId), DeliveryType.OUTBOUND)
                .orElseThrow(() -> new NoSuchElementException("Delivery not found for pallet"));
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
