package com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.mapper;

import com.warehouse.logisticsdelivery.api.event.snapshot.DeliveryEventData;
import com.warehouse.logisticsdelivery.api.event.snapshot.DeliveryStepEventData;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryEventDataMapperPort;
import com.warehouse.logisticsdelivery.domain.vo.DeliverySnapshot;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryStepSnapshot;

import java.util.Comparator;

public class DeliveryEventDataMapper implements DeliveryEventDataMapperPort {

    @Override
    public DeliveryEventData map(final DeliverySnapshot delivery) {
        return new DeliveryEventData(
                delivery.deliveryId(),
                delivery.target().type().name(),
                delivery.target().id(),
                delivery.shipmentId(),
                delivery.type().name(),
                delivery.deliveryStatus() == null ? null : delivery.deliveryStatus().name(),
                delivery.status().name(),
                delivery.originDepartmentId(),
                delivery.destinationDepartmentId(),
                delivery.pickupMethod() == null ? null : delivery.pickupMethod().name(),
                delivery.method() == null ? null : delivery.method().name(),
                delivery.pickupPointId(),
                delivery.deliveryPickupPointId(),
                delivery.signatureRequired(),
                delivery.createdAt(),
                delivery.plannedAt(),
                delivery.deliveredAt(),
                delivery.deliverySteps().stream()
                        .max(Comparator.comparingInt(DeliveryStepSnapshot::stepNumber))
                        .map(this::map)
                        .orElse(null)
        );
    }

    private DeliveryStepEventData map(final DeliveryStepSnapshot step) {
        return new DeliveryStepEventData(
                step.stepNumber(),
                step.attemptedAt(),
                step.outcome().name(),
                step.deliveryStatus() == null ? null : step.deliveryStatus().name(),
                step.userId(),
                step.supplierId(),
                step.departmentId(),
                step.vehicleId(),
                step.comment(),
                step.failureReason(),
                step.commentCode() == null ? null : step.commentCode().name()
        );
    }
}
