package com.warehouse.logisticsdelivery.application.service;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryRepository;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryLifecycleStatus;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryType;
import com.warehouse.logisticsdelivery.domain.exception.DeliveryAlreadyExistsException;
import com.warehouse.logisticsdelivery.domain.exception.DeliveryNotFoundException;
import com.warehouse.logisticsdelivery.domain.model.Delivery;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryTarget;

import java.util.Optional;

public class DeliveryFinder {

    private final DeliveryRepository deliveryRepository;

    public DeliveryFinder(final DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public Delivery get(final DeliveryId deliveryId) {
        return this.deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery " + deliveryId.getId() + " was not found"));
    }

    public Delivery getCurrent(final DeliveryTarget target, final DeliveryType type) {
        return this.deliveryRepository.findCurrentByTargetAndType(target, type)
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery for target " + target.id()
                        + " was not found"));
    }

    public Optional<Delivery> findOutbound(final DeliveryTarget target) {
        return this.deliveryRepository.findCurrentByTargetAndType(target, DeliveryType.OUTBOUND);
    }

    public Optional<Delivery> findActiveShipmentDelivery(final ShipmentId shipmentId) {
        return this.deliveryRepository.findActiveByTargetAndType(DeliveryTarget.shipment(shipmentId),
                DeliveryType.OUTBOUND);
    }

    public Delivery getShipmentDelivery(final ShipmentId shipmentId) {
        return findOutbound(DeliveryTarget.shipment(shipmentId))
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery for shipment " + shipmentId.getValue()
                        + " was not found"));
    }

    public Delivery getPalletDelivery(final String palletId) {
        return findOutbound(DeliveryTarget.pallet(palletId))
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery for pallet " + palletId + " was not found"));
    }

    public boolean hasCurrentDelivery(final DeliveryTarget target) {
        return findOutbound(target)
                .filter(delivery -> delivery.getStatus() != DeliveryLifecycleStatus.CANCELED)
                .isPresent();
    }

    public void ensureDoesNotExist(final DeliveryTarget target) {
        if (hasCurrentDelivery(target)) {
            throw new DeliveryAlreadyExistsException("Delivery for shipment " + target.id() + " already exists");
        }
    }
}
