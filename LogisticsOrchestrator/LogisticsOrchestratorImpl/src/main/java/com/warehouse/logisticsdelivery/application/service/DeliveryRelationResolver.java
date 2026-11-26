package com.warehouse.logisticsdelivery.application.service;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.logisticsdelivery.application.port.primary.result.DeliveryRelation;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryRepository;
import com.warehouse.logisticsdelivery.application.port.secondary.ShipmentServicePort;
import com.warehouse.logisticsdelivery.application.port.secondary.SourceShipment;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryRelationType;
import com.warehouse.logisticsdelivery.domain.model.Delivery;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryTarget;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class DeliveryRelationResolver {

    private final DeliveryRepository deliveryRepository;

    private final ShipmentServicePort shipmentServicePort;

    public DeliveryRelationResolver(final DeliveryRepository deliveryRepository,
                                    final ShipmentServicePort shipmentServicePort) {
        this.deliveryRepository = deliveryRepository;
        this.shipmentServicePort = shipmentServicePort;
    }

    public List<DeliveryRelation> resolve(final Delivery delivery) {
        return Stream.of(
                        shipmentRelations(delivery),
                        Optional.ofNullable(delivery.getReplacedByDeliveryId()).stream()
                                .flatMap(replacingId -> this.deliveryRepository.findById(replacingId).stream())
                                .map(replacing -> new DeliveryRelation(DeliveryRelationType.REPLACED_BY,
                                        replacing.snapshot())),
                        this.deliveryRepository.findReplacedBy(delivery.getDeliveryId()).stream()
                                .map(replaced -> new DeliveryRelation(DeliveryRelationType.REPLACES,
                                        replaced.snapshot())))
                .flatMap(Function.identity())
                .toList();
    }

    private Stream<DeliveryRelation> shipmentRelations(final Delivery delivery) {
        return Optional.ofNullable(delivery.getShipmentId()).stream().flatMap(shipmentId -> {
            final Optional<ShipmentId> sourceShipmentId = this.shipmentServicePort.findSourceShipment(shipmentId)
                    .map(SourceShipment::shipmentId);
            final Optional<ShipmentId> derivedShipmentId = this.shipmentServicePort.findDerivedShipment(shipmentId)
                    .filter(id -> !id.equals(shipmentId) && !sourceShipmentId.equals(Optional.of(id)));
            return Stream.of(
                            sourceShipmentId.stream().flatMap(id -> shipmentDeliveries(id, DeliveryRelationType.SOURCE)),
                            derivedShipmentId.stream().flatMap(id -> shipmentDeliveries(id, DeliveryRelationType.DERIVED)),
                            continuationRelations(delivery))
                    .flatMap(Function.identity());
        });
    }

    private Stream<DeliveryRelation> shipmentDeliveries(final ShipmentId shipmentId,
                                                        final DeliveryRelationType relation) {
        return this.deliveryRepository.findByTarget(DeliveryTarget.shipment(shipmentId)).stream()
                .map(related -> new DeliveryRelation(relation, related.snapshot()));
    }

    private Stream<DeliveryRelation> continuationRelations(final Delivery delivery) {
        final List<Delivery> newestFirst = this.deliveryRepository.findByTarget(delivery.getTarget()).stream()
                .filter(other -> other.getType() == delivery.getType())
                .toList();
        final Predicate<Delivery> isOtherDelivery = other -> !other.getDeliveryId().equals(delivery.getDeliveryId());
        return Stream.concat(
                newestFirst.stream().takeWhile(isOtherDelivery)
                        .map(newer -> new DeliveryRelation(DeliveryRelationType.CONTINUED_BY, newer.snapshot())),
                newestFirst.stream().dropWhile(isOtherDelivery).skip(1)
                        .map(older -> new DeliveryRelation(DeliveryRelationType.CONTINUES, older.snapshot())));
    }
}
