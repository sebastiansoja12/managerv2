package com.warehouse.logisticsdelivery.application.service;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.eventoutbox.application.port.secondary.DomainEventPublisher;
import com.warehouse.logisticsdelivery.application.port.primary.result.AssignmentBatch;
import com.warehouse.logisticsdelivery.application.port.secondary.AwaitingCourierDelivery;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryRepository;
import com.warehouse.logisticsdelivery.application.port.secondary.ShipmentServicePort;
import com.warehouse.logisticsdelivery.domain.event.DeliveryModified;
import com.warehouse.logisticsdelivery.domain.model.CourierRoster;
import com.warehouse.logisticsdelivery.domain.model.Delivery;
import com.warehouse.logisticsdelivery.domain.vo.AssignmentRunSummary;
import com.warehouse.logisticsdelivery.domain.vo.Courier;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryAssignmentCursor;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class AssignmentBatchAssigner {

    private final DeliveryRepository deliveryRepository;

    private final ShipmentServicePort shipmentServicePort;

    private final DomainEventPublisher domainEventPublisher;

    private final Clock clock;

    public AssignmentBatchAssigner(final DeliveryRepository deliveryRepository,
                                   final ShipmentServicePort shipmentServicePort,
                                   final DomainEventPublisher domainEventPublisher,
                                   final Clock clock) {
        this.deliveryRepository = deliveryRepository;
        this.shipmentServicePort = shipmentServicePort;
        this.domainEventPublisher = domainEventPublisher;
        this.clock = clock;
    }

    public AssignmentBatch assign(final List<AwaitingCourierDelivery> candidates,
                                  final DeliveryAssignmentCursor previousCursor,
                                  final int batchSize,
                                  final CourierRoster sourceRoster) {
        final CourierRoster roster = sourceRoster.copy();
        final Map<ShipmentId, String> postalCodes = this.shipmentServicePort.findRecipientPostalCodes(candidates.stream()
                .map(AwaitingCourierDelivery::delivery)
                .filter(Delivery::isAwaitingCourierAtDestination)
                .map(Delivery::getShipmentId)
                .filter(Objects::nonNull)
                .toList());
        long eligible = 0;
        long assigned = 0;
        long noCourier = 0;
        long skippedNotAtDepartment = 0;
        long skippedConcurrent = 0;
        long invalidDestination = 0;
        Instant maxEligibleAt = null;
        for (final AwaitingCourierDelivery candidate : candidates) {
            final Delivery delivery = candidate.delivery();
            if (!delivery.isInDestinationDepartment()) {
                skippedNotAtDepartment++;
                continue;
            }
            if (!delivery.isAwaitingCourierAtDestination()) {
                continue;
            }
            eligible++;
            final Instant eligibleAt = candidate.position().eventTime().atZone(this.clock.getZone()).toInstant();
            maxEligibleAt = maxEligibleAt == null || eligibleAt.isAfter(maxEligibleAt) ? eligibleAt : maxEligibleAt;
            final Optional<String> postalCode = Optional.ofNullable(postalCodes.get(delivery.getShipmentId()))
                    .map(String::trim)
                    .filter(code -> !code.isEmpty());
            if (postalCode.isEmpty()) {
                invalidDestination++;
                continue;
            }
            final Optional<Courier> courier = roster.select(postalCode.get());
            if (courier.isEmpty()) {
                noCourier++;
                continue;
            }
            delivery.assignCourier(courier.get().supplierId(), courier.get().vehicleId());
            if (!this.deliveryRepository.assignCourier(delivery)) {
                skippedConcurrent++;
                continue;
            }
            this.domainEventPublisher.publish(new DeliveryModified(delivery.snapshot(), this.clock.instant()));
            roster.registerAssignment(courier.get());
            assigned++;
        }
        final DeliveryAssignmentCursor nextCursor = candidates.isEmpty() ? previousCursor
                : candidates.get(candidates.size() - 1).position();
        return new AssignmentBatch(new AssignmentRunSummary(candidates.size(), eligible, assigned, noCourier,
                skippedNotAtDepartment, skippedConcurrent, invalidDestination, 0, 1), nextCursor, maxEligibleAt,
                candidates.size() < batchSize, roster);
    }
}
