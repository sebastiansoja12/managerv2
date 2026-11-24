package com.warehouse.logisticsdelivery.application.service;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryRepository;
import com.warehouse.logisticsdelivery.application.port.secondary.ShipmentServicePort;
import com.warehouse.logisticsdelivery.domain.event.DeliveryCreated;
import com.warehouse.logisticsdelivery.domain.event.DeliveryModified;
import com.warehouse.logisticsdelivery.domain.model.Delivery;
import com.warehouse.logisticsdelivery.domain.vo.DeliverySnapshot;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class PalletDeliveryService {

    private static final Logger log = LoggerFactory.getLogger(PalletDeliveryService.class);

    private final DeliveryRepository deliveryRepository;

    private final ShipmentServicePort shipmentServicePort;

    private final DeliveryFinder deliveryFinder;

    private final DeliveryChangeService deliveryChangeService;

    public PalletDeliveryService(final DeliveryRepository deliveryRepository,
                                 final ShipmentServicePort shipmentServicePort,
                                 final DeliveryFinder deliveryFinder,
                                 final DeliveryChangeService deliveryChangeService) {
        this.deliveryRepository = deliveryRepository;
        this.shipmentServicePort = shipmentServicePort;
        this.deliveryFinder = deliveryFinder;
        this.deliveryChangeService = deliveryChangeService;
    }

    public Delivery reopen(final Delivery palletDelivery, final String palletId,
                           final DepartmentId originDepartmentId, final DepartmentId destinationDepartmentId) {
        final DeliverySnapshot previousSnapshot = palletDelivery.snapshot();
        palletDelivery.reopenPalletDelivery(originDepartmentId, destinationDepartmentId);
        this.deliveryChangeService.saveChanged(palletDelivery, previousSnapshot, DeliveryModified::new);
        log.info("Pallet delivery {} reused for pallet {}", palletDelivery.getDeliveryId().getId(), palletId);
        return palletDelivery;
    }

    public void continueReplacedDeliveries(final Delivery palletDelivery, final List<ShipmentId> shipmentIds,
                                           final Function<Delivery, Delivery> continuation) {
        final Map<ShipmentId, Delivery> replacedDeliveries = new LinkedHashMap<>();
        this.deliveryRepository.findReplacedBy(palletDelivery.getDeliveryId())
                .forEach(replaced -> replacedDeliveries.putIfAbsent(replaced.getShipmentId(), replaced));
        shipmentIds.stream()
                .filter(replacedDeliveries::containsKey)
                .filter(shipmentId -> !this.deliveryFinder.hasCurrentDelivery(DeliveryTarget.shipment(shipmentId)))
                .filter(shipmentId -> !this.shipmentServicePort.isCanceled(shipmentId))
                .map(shipmentId -> continuation.apply(replacedDeliveries.get(shipmentId)))
                .forEach(delivery -> this.deliveryChangeService.save(delivery, DeliveryCreated::new));
    }
}
